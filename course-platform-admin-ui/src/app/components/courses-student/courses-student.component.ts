import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, catchError, throwError } from 'rxjs';
import { Course } from 'src/app/model/course.model';
import { PageResponse } from 'src/app/model/page.response.model';
import { CoursesService } from 'src/app/services/courses.service';

@Component({
  selector: 'app-courses-student',
  templateUrl: './courses-student.component.html',
  styleUrls: ['./courses-student.component.css']
})
export class CoursesStudentComponent implements OnInit {

  studentId !: number;
  pageCourses!: Observable<PageResponse<Course>>;
  pageOtherCourses!: Observable<PageResponse<Course>>;
  currentPage:number = 0;
  otherCoursesCurrentPage:number = 0;
  pageSize:number = 5;
  otherCoursesPageSize:number = 5;
  errorMessage!:string;
  otherCoursesErrorMessage!:string;

  constructor(
    private route: ActivatedRoute,
    private coursesService : CoursesService
  ) { }

  ngOnInit(): void {
    this.studentId = this.route.snapshot.params['id'];
    this.handleSearchStudentCourses();
    this.handleSearchNonEnrolledInCourses();
  }

  handleSearchStudentCourses(){
    this.pageCourses = this.coursesService.getCoursesByStudent(this.studentId, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    )
  }

  gotoPage(page:number){
    this.currentPage = page;
    this.handleSearchStudentCourses();
  }

  handleSearchNonEnrolledInCourses(){
    this.pageOtherCourses = this.coursesService.getNonEnrolledInCoursesByStudent(this.studentId, this.otherCoursesCurrentPage, this.otherCoursesPageSize).pipe(
      catchError(err => {
        this.otherCoursesErrorMessage = err.message;
        return throwError(err);
      })
    )
  }

  gotoPageOtherCourses(pageOtherCourses:number){
    this.otherCoursesCurrentPage = pageOtherCourses;
    this.handleSearchNonEnrolledInCourses();
  }
}
