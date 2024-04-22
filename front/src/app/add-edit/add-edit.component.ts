import { ChangeDetectorRef, Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CoreService } from '../core/core.service';
import { TodoService } from '../services/todo.service';
import { ToDo } from '../interface/to-do';
import { HttpEventType } from '@angular/common/http';

@Component({
  selector: 'app-add-edit',
  templateUrl: './add-edit.component.html',
  styleUrls: ['./add-edit.component.scss'],
})
export class AddEditComponent implements OnInit {
  uploadProgress = 0;
  todoForm: FormGroup;
  selectedFile: File | null = null;
  imageSrc: string | ArrayBuffer | null = null; // Selected image preview
  _filename: string | undefined;
  education: string[] = [
    'Matric',
    'Diploma',
    'Intermediate',
    'Graduate',
    'Post Graduate',
  ];

  constructor(
    private _fb: FormBuilder,
    private _todoService: TodoService,
    private _dialogRef: MatDialogRef<AddEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private _coreService: CoreService
  ) {
    this.todoForm = this._fb.group({
      id: null,
      description: '',
      fileName: '',
      fileUrl: '',
      file: null,
    });
  }

  ngOnInit(): void {
    if (this.data) {
      this.todoForm.patchValue(this.data);
      if (this.isImageFile(this.todoForm.get('fileName')?.value)) {
        this.imageSrc = this.todoForm.get('fileUrl')?.value;
      } else {
        this._filename = this.todoForm.get('fileName')?.value;
      }
    }
  }

  onFormSubmit() {
    if (this.todoForm.valid) {
      let todoData: ToDo;
      if (this.data) {
        todoData = {
          id: this.todoForm.get('id')!.value,
          description: this.todoForm.get('description')!.value,
          fileName: this.todoForm.get('fileName')!.value,
          fileUrl: this.todoForm.get('fileUrl')!.value,
        };
      } else {
        todoData = {
          description: this.todoForm.get('description')!.value,
        };
      }
      if (this.data) {
        this._todoService.updateToDo(todoData, this.selectedFile).subscribe({
          next: (event: any) => {
            if (event.type === HttpEventType.UploadProgress) {
              // Calculate and update upload progress
              this.uploadProgress = Math.round(
                (100 * event.loaded) / event.total
              );
            } else if (event.type === HttpEventType.Response) {
              this._coreService.openSnackBar('File updated!');
              this.uploadProgress = 0;
              this._dialogRef.close(true);
            }
          },
          error: (err: any) => {
            console.error(err);
          },
        });
      } else {
        this._todoService.createToDo(todoData, this.selectedFile).subscribe({
          next: (event: any) => {
            if (event.type === HttpEventType.UploadProgress) {
              // Calculate and update upload progress
              this.uploadProgress = Math.round(
                (100 * event.loaded) / event.total
              );
            } else if (event.type === HttpEventType.Response) {
              this._coreService.openSnackBar('File added successfully');
              this.uploadProgress = 0;
              this._dialogRef.close(true);
            }
          },
          error: (err: any) => {
            console.error(err);
          },
        });
      }
    }
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file.type.startsWith('image')) {
      this._filename = '';
      const reader = new FileReader();
      reader.onload = (e) => (this.imageSrc = reader.result as string);
      reader.readAsDataURL(file);
      this.selectedFile = file;
    } else {
      this.imageSrc = '';
      const reader = new FileReader();
      reader.readAsDataURL(file);
      this.selectedFile = file;
      this._filename = this.selectedFile?.name;
    }
  }

  isImageFile(fileName: string): boolean {
    if (fileName) {
      const imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp'];
      const lastPeriodIndex = fileName.lastIndexOf('.');
      if (lastPeriodIndex !== -1) {
        const fileExtension = fileName.substr(lastPeriodIndex).toLowerCase();
        return imageExtensions.includes(fileExtension);
      }
      return false; // Return false if file extension is not found
    }
    return false;
  }
}
