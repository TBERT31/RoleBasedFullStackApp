import {Component, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { Observable, catchError, throwError } from 'rxjs';
import { Course } from 'src/app/model/course.model';
import { Instructor } from 'src/app/model/instructor.model';
import { PageResponse } from 'src/app/model/page.response.model';
import { CoursesService } from 'src/app/services/courses.service';
import { InstructorsService } from 'src/app/services/instructors.service';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css']
})

export class CoursesComponent implements OnInit {

  searchFormGroup !: FormGroup;
  courseFormGroup!:FormGroup;
  updateCourseFormGroup!:FormGroup;
  pageCourses$!:Observable<PageResponse<Course>>;
  instructors$!:Observable<Array<Instructor>>;
  currentPage:number=0;
  pageSize:number=5;
  errorMessage !: string;
  errorInstructorsMessage !: string;
  submitted: boolean = false;
  defaultInstructor!:Instructor;

  constructor(
    private modalService: NgbModal,
    private fb: FormBuilder,
    private coursesService : CoursesService,
    private instructorsService: InstructorsService
    ) {
  }

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control('')
    });
    this.courseFormGroup = this.fb.group({
      courseName: ["", Validators.required],
      courseDuration: ["", Validators.required],
      courseDescription: ["", Validators.required],
      instructor: [null, Validators.required]
    });
    this.handleSearchCourses();
  }

  getModal(content: any) {
    this.submitted=false;
    this.fetchInstructors();
    this.modalService.open(content, {size: 'xl'})
  }

  handleSearchCourses(){
    let keyword = this.searchFormGroup.value.keyword;
    this.pageCourses$ = this.coursesService.searchCourses(keyword, this.currentPage, this.pageSize).pipe(
      catchError(err => {
          this.errorMessage = err.message;
          throw throwError(err);
      })
    )
  }

  gotoPage(page:number){
    this.currentPage = page;
    this.handleSearchCourses();
  }

  handleDeleteCourse(c:Course){
    let conf = confirm("Are you sure ?");
    if(!conf) return;

    this.coursesService.deleteCourse(c.courseId)
      .subscribe({
        next:()=>{
          this.handleSearchCourses();
        },
        error: err => {
          alert(err.message);
          console.log(err);
        }
      });
  }

  fetchInstructors(){
    this.instructors$ = this.instructorsService.findAllInstructors().pipe(
      catchError(err => {
          this.errorInstructorsMessage = err.message;
          throw throwError(err);
      })
    )
  }

  onCloseModal(modal: any){
    modal.close();
    this.courseFormGroup.reset();
  }

  onSaveModal(modal: any){
      this.submitted = true;
      if(this.courseFormGroup.invalid) return;
      this.coursesService.saveCourse(this.courseFormGroup.value).subscribe({
        next: () => {
          alert("Success Saving Course");
          this.handleSearchCourses();
          this.courseFormGroup.reset();
          this.submitted=false;
          modal.close();
        }, error: err => {
          alert(err.message);
        },
      })
  }

  getUpdateModel(c:Course, updateContent: any){
    this.fetchInstructors();
    this.updateCourseFormGroup = this.fb.group({
      courseId: [c.courseId, Validators.required],
      courseName: [c.courseName, Validators.required],
      courseDuration: [c.courseDuration, Validators.required],
      courseDescription: [c.courseDescription, Validators.required],
      instructor: [c.instructor, Validators.required],
    });
    this.defaultInstructor = this.updateCourseFormGroup.controls['instructor'].value;
    this.modalService.open(updateContent, {size:'xl'});
  }

  onUpdateModal(updateModal:any){
    this.submitted=true;
    if(this.updateCourseFormGroup.invalid) return;
    this.coursesService.updateCourse(this.updateCourseFormGroup.value, this.updateCourseFormGroup.value.courseId).subscribe({
      next:()=>{
        alert("Success Updating Course");
        this.handleSearchCourses();
        this.submitted=false;
        updateModal.close();
      }, error: err => {
        alert(err.message);
      },
    })
  }
}
