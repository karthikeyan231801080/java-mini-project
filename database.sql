-- Step 1: Create the database
CREATE DATABASE HotelManagementSystem;

-- Step 2: Use the created database
USE HotelManagementSystem;

-- Step 3: Create the tables

-- Table 1: Customers
CREATE TABLE Customers (
    CustomerID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE,
    PhoneNumber VARCHAR(15),
    Address VARCHAR(255),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table 2: Rooms
CREATE TABLE Rooms (
    RoomID INT AUTO_INCREMENT PRIMARY KEY,
    RoomType ENUM('Single', 'Double', 'Triple', 'Luxury') NOT NULL,
    BedCount INT NOT NULL,
    PricePerNight DECIMAL(10, 2) NOT NULL,
    IsAvailable BOOLEAN DEFAULT TRUE,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table 3: Bookings
CREATE TABLE Bookings (
    BookingID INT AUTO_INCREMENT PRIMARY KEY,
    CustomerID INT NOT NULL,
    RoomID INT NOT NULL,
    CheckInDate DATE NOT NULL,
    CheckOutDate DATE NOT NULL,
    TotalAmount DECIMAL(10, 2),
    BookingStatus ENUM('Pending', 'Confirmed', 'Cancelled') DEFAULT 'Pending',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID),
    FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID)
);

-- Table 4: Payments
CREATE TABLE Payments (
    PaymentID INT AUTO_INCREMENT PRIMARY KEY,
    BookingID INT NOT NULL,
    PaymentMethod ENUM('Cash', 'Credit Card', 'Debit Card', 'Online Transfer') NOT NULL,
    AmountPaid DECIMAL(10, 2) NOT NULL,
    PaymentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (BookingID) REFERENCES Bookings(BookingID)
);

-- Table 5: Staff (optional)
CREATE TABLE Staff (
    StaffID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Position VARCHAR(50),
    Email VARCHAR(100) UNIQUE,
    PhoneNumber VARCHAR(15),
    HireDate DATE,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Step 4: Insert sample data (optional)

-- Sample data for Rooms
INSERT INTO Rooms (RoomType, BedCount, PricePerNight, IsAvailable)
VALUES
    ('Single', 1, 2000.00, TRUE),
    ('Double', 2, 3500.00, TRUE),
    ('Triple', 3, 5000.00, TRUE),
    ('Luxury', 2, 10000.00, TRUE);

-- Sample data for Customers
INSERT INTO Customers (FirstName, LastName, Email, PhoneNumber, Address)
VALUES
    ('John', 'Doe', 'john.doe@example.com', '1234567890', '123 Main Street'),
    ('Jane', 'Smith', 'jane.smith@example.com', '0987654321', '456 Elm Street');
