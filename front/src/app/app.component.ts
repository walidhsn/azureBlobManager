import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AddEditComponent } from './add-edit/add-edit.component';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { CoreService } from './core/core.service';
import { TodoService } from './services/todo.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'crud-app';
  displayedColumns: string[] = [
    'id',
    'description',
    'fileName',
    'fileUrl',
    'action',
  ];
  dataSource!: MatTableDataSource<any>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private _dialog: MatDialog,
    private _todoService: TodoService,
    private _coreService: CoreService
  ) {}

  ngOnInit(): void {
    this.getList();
  }

  openAddEditForm() {
    const dialogRef = this._dialog.open(AddEditComponent);
    dialogRef.afterClosed().subscribe({
      next: (val) => {
        if (val) {
          this.getList();
        }
      },
    });
  }

  getList() {
    this._todoService.getAllToDos().subscribe({
      next: (res) => {
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: console.log,
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  deleteTodo(id: number) {
    if (confirm('Are you sure you want to delete this file?')) {
      this._todoService.deleteToDo(id).subscribe({
        next: (res) => {
          this._coreService.openSnackBar('File deleted!', 'done');
          this.getList();
        },
        error: console.log,
      });
    }
  }

  openEditForm(data: any) {
    const dialogRef = this._dialog.open(AddEditComponent, {
      data,
    });

    dialogRef.afterClosed().subscribe({
      next: (val) => {
        if (val) {
          this.getList();
        }
      },
    });
  }

  downloadFile(blobItem: string): void {
    if (confirm('Would you like to download the file?')) {
      this._todoService.downloadFile(blobItem).subscribe((blob) => {
        // Create a download link and trigger the download
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        document.body.appendChild(a);
        a.style.display = 'none';
        a.href = url;
        a.download = blobItem; // Set the desired file name here
        a.click();
        window.URL.revokeObjectURL(url);
      });
    }
  }
}
