# Last room Booking API

### Short description
This is a Java API developed to booking a specific room in a Hotel in Cancun. <br/>
There is no special tips to run this project. <br/>
Its possible to find all the endpoints description on this same file, on section 'Endpoints'. <br/>
The code is available on GitHub (https://github.com/pedroventura88/hotel-room-booking), branch Master.

### Used technologies - Reference Documentation
For further reference, please consider the following sections about what was used on developemnt of this API:
* [Java 11](https://docs.oracle.com/en/java/javase/11/docs/api/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/)
* [H2](https://www.h2database.com/html/main.html) - in memory persistence database
* [Maven](https://maven.apache.org/)

### Data initialization
* The application have an in memory database(H2) and when started, will persist 5 objects on it.
That information can be found on class DBInitializer.
* The configuration can be found on resources/application.properties

### API Documentation
* Considering the requirements described at the challenge, the documentation about the API access endpoints will be described
on this same README file. **However, if it was an real project, I had used the OpenApi3.0 specification to write the contracts.**

### Unit Tests and Integration test
* No Unit tests or Integration test developed, considering the requirements of the challenge.
**However, if it was an real project I had written the test cases using Junit and Mockito.**

Endpoints
-
To help on test, postman collections (*Booking.postman_collection.json*) to make the requests, are available at root, on postman directory. <br/>
For clarification, the format of date used is YYYY-MM-DD
<br/>
* **bookRoom - POST** <br/>
http://localhost:8080/v1/bookings <br/>
All the attributes on the RequestBody are mandatory, including the customerName. For the simplicity of the test, the customer
is being represented by an simple String. **If it was in a real case, I had used an Entity to represent customers.** <br/>
This endpoint uses a RequestBody with the structure example bellow:<br/>
return expected: BookingDto object created with description and Id.
```
{
    "checkkIn":"2022-09-03",
    "checkOut":"2022-09-04",
    "customerName":"Pedro Ventura"
}
```


* **checkAvailability - GET** <br/>
http://localhost:8080/v1/bookings/check <br/>
Use of a mandatory queryParam (date) that represents the date to be checked (format YYYY-MM-DD). <br/>
Example: http://localhost:8080/v1/bookings/check?date=2022-09-03 <br/>
return expected: if is available, will return the message 'The day XXXX-XX-XX is available for booking'

* **getBookings - GET** <br/>
http://localhost:8080/v1/bookings <br/>
This endpoint is not included on the challenge requirements, but it helps a little when you are testing. Also is important 
to mention that this functionality dont have any pagination. <br/>
There are no parameters to be passed on that functionality. <br/>
return expected: List of all bookings not paginated.
<br/>
* **updateBooking - PUT** <br/>
http://localhost:8080/v1/bookings/{id} <br/>
Example: http://localhost:8080/v1/bookings/4 <br/>
This endpoint has a path param representing the bookingId to be updated, and also a RequestBody that will contains the values to be used. <br/>
Follow bellow an example of the RequestBody:
return expected: BookingDto object updated with description and Id.
```
{
    "checkkIn":"2022-09-03",
    "checkOut":"2022-09-04",
    "customerName":"Pedro Ventura"
}
```


* **cancelBooking - PATCH** <br/>
http://localhost:8080/v1/bookings/{id} <br/>
Example: http://localhost:8080/v1/bookings/4 <br/>  
This endpoint has a path param representing the bookingId to be canceled. To keep the track of bookings,
I've chosen to make a logical deletion and not physical, marking the object with the flag isCanceled to true. <br/>
return expected: if successfully canceled, will return a 204 message without content.



