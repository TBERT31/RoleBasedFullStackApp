import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, catchError, throwError } from 'rxjs';
import { Instructor } from 'src/app/model/instructor.model';
import { PageResponse } from 'src/app/model/page.response.model';
import { InstructorsService } from 'src/app/services/instructors.service';

@Component({
  selector: 'app-teachers',
  templateUrl: './teachers.component.html',
  styleUrls: ['./teachers.component.css']
})


export class TeachersComponent implements OnInit {
  searchFormGroup!:FormGroup;
  pageInstructors !: Observable<PageResponse<Instructor>>;
  errorMessage !: string;
  currentPage:number=0;
  pageSize:number=10;


  constructor(
    private modalService: NgbModal,
    private fb: FormBuilder,
    private instructorsService : InstructorsService
  ) {}

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control('')
    });
    this.handleSearchInstructors();
  }


  getModal(content: any){
    this.modalService.open(content, { size: 'xl' })
    console.log("Hello world")
  }

  handleSearchInstructors(){
    let keyword = this.searchFormGroup.value.keyword;
    this.pageInstructors = this.instructorsService.searchInstructors(keyword, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    )
  }

}

