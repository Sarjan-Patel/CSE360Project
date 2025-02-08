# CSE360Project
```
Project Structure:
CSE360-UserManagement/
│── src/
│   ├── databasePart1/
│   │   └── DatabaseHelper.java
│   │
│   ├── application/
│   │   ├── AdminHomePage.java
│   │   ├── UserLoginPage.java
│   │   ├── SetupAccountPage.java
│   │   └── ... (other application classes)
│
│── README.md
│── pom.xml
```
---

## Overview

#### The CSE360 User Management System is a JavaFX-based application designed to manage user authentication, role assignments, and invitation-based user registration. The system securely integrates with an H2 database to store and manage user credentials.

## Features 📍

### User Registration & Login

Register using invitation codes

Secure login with password validation


### Role Management 📓 

Admins can assign multiple roles (staff, instructor, admin, user)

Flexible user role manipulation


### One-Time Password (OTP) System 🔑 

Password reset functionality

Secure temporary access mechanism


### Admin Privileges 😏

Generate invitation codes

Manage user roles

Delete users (with admin protection)


### Password Security 👮

Strong password validation

Finite State Machine (FSM) based password strength checker

--- 
## User Stories and Contributions 🏆

Each member of the team has been allocated one or more user stories and is responsible for their development and integration.

### 1. First User Setup - Medha

The first user can establish their username, password, and role as an admin.

After setup, the user must log in again.

### 2. Assigning Multiple Roles - Nia

Users can be assigned one or more roles based on their responsibilities.

### 3. Single Role Auto-Login - Medha

Users with only one assigned role are taken directly to their home screen after logging in.

### 4. Multiple Role Selection - Janelle

Users with multiple roles select which role they want to play after logging in.

### 5. Logging Out - Nia

Users can log out or switch roles when needed.

### 6. User Invitation - Riana and Janelle

Admins can invite users to the system using a one-time code with an expiration date.

### 7. One-Time Password (OTP) for Reset - Riana

Admins can issue a one-time password for users to reset their password.

### 8. User Deletion - Sarjan

Admins can remove users from the system, but cannot delete themselves.

Users must confirm deletion with an "Are you sure?" prompt.

### 9. Role Management - Sarjan

Admins can add or remove roles for each user.

There must always be at least one admin in the system.

---

## How It Works

### Database Setup 📊 

The system connects to an H2 database (FoundationDatabase) and creates the necessary tables (cse360users and InvitationCodes) automatically when started.

### User Registration & Login Flow

The first user is prompted to create an admin account.

This admin can generate invitation codes for new users.

### New User Registration 👤

Users enter a username, password, and invitation code.

If valid, they are registered in the database with the role associated with the invitation code.

### User Login 👤

Users enter their credentials.

If authentication is successful, they are directed to their respective home pages (AdminHomePage or UserHomePage).

### One-Time Password (OTP) Reset 🔑

Users who forget their password can request an OTP.

Upon entering the OTP, they must create a new password.

## Admin Privileges 🏫

### Admins can:

Invite new users to join the system via a one-time code with an expiration date (Implemented by Janelle & Riana).

Assign multiple roles to users, allowing them to access different system functionalities (Implemented by Nia).

Set a one-time password for users who forgot their credentials, requiring them to reset it upon login (Implemented by Riana).

Delete user accounts, except for other admins, ensuring that no user remains without administrative oversight (Implemented by Sarjan).

Add or removing roles for users while ensuring that at least one admin always exists (Implemented by Sarjan).

Screencast1

Zoom Link: https://asu.zoom.us/rec/share/gbrx_slLyKK22s9JuavIp-S_udHPfyV5VQ61g-QN03gc6xSQJdlriKMd53VCntJi.mbxkj9kDcUMv5MLD?startTime=1738994563000
 Passcode: &h&n2c0M


Screencast2
Zoom Link: https://asu.zoom.us/rec/share/B5R87pXVtDRKhFwK8sRceYS2G_BwXFqBc1gsZJc4xuELrYG_3unYol1pZ84W-v4v.Dy6G2SdhG1a85JMB?startTime=1738997567000 Passcode: =2rLC=V?

