import { AbstractControl, AsyncValidatorFn, ValidationErrors, ValidatorFn } from "@angular/forms";
import { map, Observable } from "rxjs";
import { UsersService } from "src/app/services/users.service";

export class EmailExistsValidator {
    static validate(userService: UsersService) : AsyncValidatorFn {
        return (control : AbstractControl): Observable<ValidationErrors | null> => {
            return userService.checkIfEmailExist(control.value).pipe(
                map((result: boolean) => result ? {emailAlreadyExists:true} : null)
            )
        }
    }
}