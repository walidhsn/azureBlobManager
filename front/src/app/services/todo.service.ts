import { Injectable } from '@angular/core';
import { ToDo } from '../interface/to-do';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../config/endpoints';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class TodoService {
  private baseUrl = API_ENDPOINTS.backEnd + 'todo';
  constructor(private http: HttpClient) {}

  getAllToDos(): Observable<ToDo[]> {
    return this.http.get<ToDo[]>(this.baseUrl);
  }

  getToDoById(id: number): Observable<ToDo> {
    return this.http.get<ToDo>(`${this.baseUrl}/${id}`);
  }

  createToDo(todo: ToDo, file: File | null): Observable<any> {
    const formData: FormData = new FormData();
    formData.append(
      'todo',
      new Blob([JSON.stringify(todo)], {
        type: 'application/json',
      })
    );
    if (file) {
      formData.append('file', file, file.name);
    }
    return this.http.post(this.baseUrl, formData, { reportProgress: true, observe: 'events' });
  }

  updateToDo(todo: ToDo, file: File | null): Observable<any> {
    const formData: FormData = new FormData();
    formData.append(
      'todo',
      new Blob([JSON.stringify(todo)], {
        type: 'application/json',
      })
    );
    if (file) {
      formData.append('file', file, file.name);
    }
    return this.http.put(this.baseUrl, formData, { reportProgress: true, observe: 'events' });
  }

  deleteToDo(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  downloadFile(fileName: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/download/${fileName}`, {
      responseType: 'blob',
    });
  }
}
