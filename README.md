# Quote Library

### What will the application do?

Quote Library is a desktop Java application that allows users to organize and 
view their favorite quotes.

### Who will use it?

Users are people who enjoy reading and collecting quotes, whether from famous 
people or anonymous authors. 

### Why is this project of interest to you?

I am building Quote Library because I enjoy collecting quotes, but I don't have a 
tool for **organizing** and **viewing** them. I currently use my Apple Notes app 
to store a long list of quotes, but they are locked in reverse chronological order 
by date added so I rarely see old quotes again.  

### What are the user stories?

- As a user, I want to be able to **add** a quote (along with the author) to my 
library.
- As a user, I want to be able to **edit** a quote in my library.
- As a user, I want to be able to **remove** a quote from my library.
- As a user, I want to be able to **view** all my quotes in a list.
- As a user, I want to be able to **save** all my quotes to a file.
- As a user, I want to be able to **load** all my quotes from a file.

### Phase 4, Task 2

- Option: Test and design a robust class in the model package that throws at least one
checked exception.
- See the robust Library class with the addQuote() and editQuote() methods that throw 
EmptyException and DuplicateException through the validateQuote(Quote) helper method.
- See the LibraryTest suite, specifically the following unit tests: testAddQuote(), 
testAddQuoteDuplicate(), testAddQuoteEmpty, testAddQuoteHighVolume, and testEditQuote().

### Phase 4, Task 3

![UML Class Diagram](https://github.com/julienlapointe/quote-library/blob/master/UML_Design_Diagram.png?raw=true)

Given more time, I would refactor the GUI class. It is currently 700 lines long. I would 
break the five event listener / handler classes into their own files. Additionally, there is 
duplicate code among the seven methods that produce sound in the GUI class. I would create a
single helper method for producing sound. 

### Citations
- Persistence code based on https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
- Swing code based on https://www.lynda.com/Java-tutorials/Learn-Java-Swing/592496-2.html 
(Lynda.com's Learn Java with Swing course)
