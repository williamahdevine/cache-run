- [Cache Run](#cache-run)
	- [The Team](#the-team)
	- [Description](#description)
	- [Features](#features)
	- [Libraries](#libraries)
	- [Requirements](#requirements)
	- [Installation Notes](#installation-notes)
	- [Final Project Status](#final-project-status)
	    - [Minimum Functionality](#minimum-functionality)
    	- [Expected Functionality](#expected-functionality)
    	- [Bonus Functionality](#bonus-functionality)
    - [Code Examples](#code-examples)
    - [Functional Decomposition](#functional-decomposition)
    - [High-level Organization](#high-level-organization)
	- [Clickstreams](#clickstreams)
		- [Restaurant Detail Screen](#restaurant-detail-screen)
		- [Coupon Sharing](#coupon-sharing)
		- [View History](#view-history)
		- [Logout Screen](#logout-screen)

# CacheRun 

CacheRun is an immersive augment reality (AR) application that is a platform for vendors to advertise sales to customers. The application contains AR coupons from various vendors that users are able to travel around and collect. As the user enters the radius of a coupon, they can see how close they are to it. Once the user is close enough to the coupon it will appear on their screen in AR and the user can save it to their list of saved coupons. 

## Group members

| Name                   | Banner ID  | Email               |
| ---------------------- | ---------- | ------------------- |
| Kyle Burgess           | B00760561  | k.burgess@dal.ca    |
| Serena Drouillard      | B00        | aa123456@dal.ca     |
| Member 3               | B00123456  | aa123456@dal.ca     |

## Description

Coupons are an effective means of advertising and increasing sales. A popular means for a business to provide discounts are through "hurdle" promotions. This is similar to a compnay offering a mail-in rebate which provides the consumer a discount if they are willing to put the effort into taking advantage of it. 

CacheRun takes the idea of "hurdle" promotions to the streets. The app allows vendors to place AR coupons at set geo-locations. Each coupon placed features a distance threshold, once a user is within this radius they are able to display the coupon in AR and ultimately save the coupon to be used later. 

So then, CacheRun will provide benefits for both vendors, as they can advertise their sales and promotions, and consumers as they can reap the benefits of these sales and promotions.


### Users

Coupons are leveraged by all age groups, however, the CacheRun team is aware that an AR app may not be as appealing to older generations. As such, CacheRun's targeted age demographic is 18-40 year olds.

### Features

- Able to display coupons in the user's area
- Able to display the AR coupon once the user selects it
- Able to collect coupons that displayed in AR


## Libraries

**[ARCore:](https://developers.google.com/ar/develop/java/quickstart)** ARCore is a software development kit developed by Google that allows for augmented reality applications to be built.

**[Sceneform:](https://developers.google.com/ar/develop/java/sceneform/)** Sceneform makes it straightforward to render realistic 3D scenes in AR and non-AR apps, without having to learn OpenGL.


## Requirements

**Minimum Requirements**
- Android 8.0 (Android 10.0 is targeted)
- Camera Access
- Location Access

## Installation Notes

Only the included apk is reuired to run

**Note:** The submitted version of CacheRun will feature coupons in the collection radius of the Goldberg Computer Science building. If testing/marking is performed elsewhere, [simply update the emulator's location](https://developer.android.com/studio/run/emulator#extended) or update the latitude and longitude of coupons in the code:

```kotlin
private fun buildCoupons() {
    piggyCoupon.hardCodedLocation.latitude= 44.673524
    piggyCoupon.hardCodedLocation.longitude = -63.614440
    availableCouponList.add(piggyCoupon)

    pizzaCoupon.hardCodedLocation.latitude= 44.673524
    pizzaCoupon.hardCodedLocation.longitude = -63.614440
    availableCouponList.add(pizzaCoupon)

    bookCoupon.hardCodedLocation.latitude= 44.673524
    bookCoupon.hardCodedLocation.longitude = -63.614440
    availableCouponList.add(bookCoupon)
}
```

## Final Project Status

The CacheRun team was successfully able to adhere to our timeline throughout the process. As such, the team was able to complete all of Minimum Functionallity and Expected Function. Bonus Functionality was completed save for one task. 

Unfortunately, not much time was able to be dedicated to UI/UX polish.

### Minimum Functionality
- Display hard-coded pins at a predetermined, hardcoded GPS location that are always visible to the user. (Completed)

### Expected Functionality
- Replace pins with 3D coupon models
- Calculate distance from user to coupon
- Once user is within range of coupon, the coupon becomes visible
- User can collect 

### Bonus Functionality
- Feature 1 name (Completed)
- Feature 2 name (Partially Completed)
- Feature 3 (Not Implemented)


## Code Examples

You will encounter roadblocks and problems while developing your project.
Share 2-3 'problems' that your team encountered.
Write a few sentences that describe your solution and provide a code snippet/block
that shows your solution. Example:

**Problem 1: We needed to detect shake events**

A short description.
```
// The method we implemented that solved our problem
public void onSensorChanged(SensorEvent event) {
    now = event.timestamp;
    x = event.values[0];
    y = event.values[1];
    z = event.values[2];

    if (now - lastUpdate > 10) {
        force = Math.abs(x + y + z - lastX - lastY - lastZ);
        if (force > threshold) {
            listener.onShake(force);
        }
        lastX = x;
        lastY = y;
        lastZ = z;
        lastUpdate = now;
    }
}

// Source: StackOverflow [3]
```

## Functional Decomposition

A diagram and description of the application's primary functions and decomposition.
[TODO: Identify and describe how this differs from High-level Organization.]

## High-level Organization

The hierarchy or site map of the application.
This can be reused from Updates 1 and 2, updated with any changes made since then.

## Clickstreams

A brief description of the common use cases.
This can be reused from Updates 1 and 2, updated with any changes made since then.

## Layout

Wire-frames of all the primary views and a brief description describing what each is for.
This can be reused from Updates 1 and 2, updated with any changes made since then.

## Prototypes

If you did low-fidelity or high-fidelity prototypes, document the process here,
including the results of your user testing. (Otherwise, delete this section.)

## Implementation

Screenshots of all the primary views (screens) and a brief discussion of the
interactions the user performs on the screens.

## Future Work

A discussion of how the implementation can be extended or improved if you had more
time and inclination to do so.


## Sources

Use IEEE citation style. Some examples:

[1] J. Moule, _Killer UX Design: Create User Experiences to Wow Your Visitors_. Sitepoint, 2012.

[2] _Ngspice_. (2011). [Online]. Available: http://ngspice.sourceforge.net

[3] "Detect shaking of device in left or right direction in android?", StackOverflow.
    https://stackoverflow.com/a/6225656 (accessed July 12, 2019).

What to include in your project sources:
- Stock images
- Design guides
- Programming tutorials
- Research material
- Android libraries
- Everything listed on the Dalhousie [*Plagiarism and Cheating*](https://www.dal.ca/dept/university_secretariat/academic-integrity/plagiarism-cheating.html)




































