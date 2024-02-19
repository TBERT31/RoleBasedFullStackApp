import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, catchError, throwError } from 'rxjs';
import { Course } from 'src/app/model/course.model';
import { Instructor } from 'src/app/model/instructor.model';
import { PageResponse } from 'src/app/model/page.response.model';
import { CoursesService } from 'src/app/services/courses.service';

@Component({
  selector: 'app-courses-instructor',
  templateUrl: './courses-instructor.component.html',
  styleUrls: ['./courses-instructor.component.css']
})
export class CoursesInstructorComponent implements OnInit {
  instructorId!:number;
  currentInstructor!:Instructor;
  pageCourses !: Observable<PageResponse<Course>>;
  currentPage : number = 0;
  pageSize : number = 5;
  errorMessage!:string;

  constructor(
    private route : ActivatedRoute,
    private coursesService : CoursesService
  ) { }

  ngOnInit(): void {
    this.instructorId = this.route.snapshot.params['id'];
    this.fillCurrentInstructor();
    this.handleSearchInstructorCourses();
  }

  private fillCurrentInstructor(){
    this.currentInstructor = {
      instructorId : this.instructorId,
      firstName: "",
      lastName: "",
      summary: "",
      user: {email: "",password: ""}
    }
  }

  private handleSearchInstructorCourses(){
    this.pageCourses = this.coursesService.getCoursesByInstructor(this.instructorId, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    )
  }


}
