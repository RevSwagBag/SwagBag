import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Product } from '../models/product';
import { environment } from '../../environments/environment';

interface Cart {
  cartCount: number;
  products: {
    product: Product;
    quantity: number;
  }[];
  totalPrice: number;
}

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private productUrl: string = '/api/product';

  private _cart = new BehaviorSubject<Cart>({
    cartCount: 0,
    products: [],
    totalPrice: 0.0,
  });

  private _cart$ = this._cart.asObservable();

  get cart(): Cart {
    return this._cart.getValue();
  }

  getCart(): Observable<Cart> {
    return this._cart$;
  }

  setCart(latestValue: Cart) {
    return this._cart.next(latestValue);
  }

  constructor(private http: HttpClient) {}

  public getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(
      environment.baseUrl + this.productUrl,
      {
        headers: environment.headers,
        withCredentials: environment.withCredentials,
      }
    );
  }

  public getSingleProduct(id: number): Observable<Product> {
    return this.http.get<Product>(
      environment.baseUrl + this.productUrl + '/' + id,
      {
        headers: environment.headers,
        withCredentials: environment.withCredentials,
      }
    );
  }

  /**
   * Get all products that contain the search query in the product name
   * 
   * @param query The string query to search
   * @returns An Observable containing an array of matching products
   */
  public getProductsByQuery(query: string): Observable<Product[]> {
    return this.http.get<Product[]>(
      environment.baseUrl + this.productUrl + "/search/" + query,
      {
        headers: environment.headers,
        withCredentials: environment.withCredentials
      }
    );
  }

  public purchase(
    products: { id: number; quantity: number }[]
  ): Observable<any> {
    const payload = JSON.stringify(products);
    return this.http.patch<any>(
      environment.baseUrl + this.productUrl,
      payload,
      {
        headers: environment.headers,
        withCredentials: environment.withCredentials,
      }
    );
  }

  public addNewProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(
      environment.baseUrl + this.productUrl,
      product,
      {
        headers: environment.headers,
        withCredentials: environment.withCredentials,
      }
    );
  }

  public updateProduct(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(
      environment.baseUrl + this.productUrl + '/' + id,
      product,
      {
        headers: environment.headers,
        withCredentials: environment.withCredentials,
      }
    );
  }
}
