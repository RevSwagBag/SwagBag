import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  authUrl: string = `${environment.baseUrl}/auth`;
  loggedIn: boolean = false;

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    const payload = { email: email, password: password };
    const res = this.http.post<any>(`${this.authUrl}/login`, payload, {
      headers: environment.headers,
      withCredentials: environment.withCredentials,
    });
    res.subscribe((data) => {
      this.loggedIn = true;
      sessionStorage.setItem(
        'loggedInUser',
        JSON.stringify({ id: data.id, name: data.firstName })
      );
    });

    return res;
  }

  logout(): void {
    this.http.post(`${this.authUrl}/logout`, null);
    sessionStorage.removeItem('loggedInUser');
  }

  register(
    firstName: string,
    lastName: string,
    email: string,
    password: string
  ): Observable<any> {
    const payload = {
      firstName: firstName,
      lastName: lastName,
      email: email,
      password: password,
    };
    return this.http.post<any>(`${this.authUrl}/register`, payload, {
      headers: environment.headers,
    });
  }

  // changePassword(form: {
  //   oldPassword: string;
  //   newPassword: string;
  // }): Observable<any> {
  //   alert(`Password Changed! Success!
  //   Email: ${form.oldPassword}
  //   Password: ${form.newPassword}
  //   `);
  //   return of(form);
  //   // return this.http.post<any>(`${this.authUrl}/change-password`, form, {headers: environment.headers})
  // }

  changePassword(form: {
    oldPassword: string;
    newPassword: string;
    confirmPassword: string;
  }): Observable<any> {
      /*alert(`Password Changed! Success!
        Email: ${form.oldPassword}
        Password: ${form.newPassword}
        `);*/
      //return of(form);
      const payload = {
        oldPassword: form.oldPassword,
        newPassword: form.newPassword,
      };
      // return this.http.post<any>(
      //   `${this.authUrl}/api/user/change-password`,
      return this.http.patch<any>(
        `${environment.baseUrl}/api/user/change-password`,
        payload,
        {
          headers: environment.headers,
          withCredentials: environment.withCredentials,
        }
      );

    //return this.http.post<any>(`${this.authUrl}/change-password`, form, {headers: environment.headers})
  }
}
