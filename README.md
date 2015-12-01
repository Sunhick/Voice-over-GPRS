# Voice-over-GPRS

## Introduction

The conventional calling system, be it wired or wireless is a very costly technology when it comes to long distance calling or 
trunk calling. Being costly is the inherent property of this technology due to its circuit switching nature. 
But Voice Over Internet Protocol (VOIP) has proved to be a boon for long distance callers, but till recently VOIP has a 
limitation of being immobile, i.e. both the Caller and the Callee must be seated in front of his workstation or Laptop. 
Voice over GPRS enables communication between 2 parties on the move at an affordable price that is economical compared to 
the voice over the GSM networks. The users in conventional mobile networks are charged on a per minute basis to make 
calls and in some countries even to receive calls. Problems of the current system(VoIP) and GSM networks- Immobility and 
Expensive.

## New system: Voice Over GPRS

* Since mobile phones are used, immobility can be overcome.

* Now-a-days packet data is affordable and calls can be made inexpensively through GPRS compared to GSM.

## Existing system

* Voice over IP is currently being used in Skype, g-talk, Nimbuzz, etc.

* VoIP uses Packet switching technology.

* GSM networks are currently used widely for making calls. 

* GSM calls are based on  circuit switching technology

## Drawbacks of existing system

* The main drawback of VoIP is that they have to be loaded on PCs. The user is confined to a particular location.

* The cost per call on GSM networks is very expensive, especially calls made overseas.

## Proposed System

**The Voice over GPRS includes 3 subsystems:**

* Voice Mail is a feature that helps a mobile user to leave a voice message when other user with which he wishes to communicate is offline.

* VoiceChat allows a user to record his voice and send it to the web server that routes this voice to required destination. The destination plays out recorded voice.

* Virtual Class Room serves as a means for delivering lecture by professor to his students across the globe.

## Functional Requirements:

* The application should comprise of a server (Internet Connected PC) and a client (GPRS-enabled mobile phone and also Java(KVM) Enabled Mobile Phone). 

* AMR encoding and decoding – Record and play.

* The user must be provided with an easy-to-use, intuitive front-end which enables user to avail the functionalities of the application.

## Non Functional Requirements:

* The application should not hamper the execution of other programs running on the system.

* The response time should be negligible.

* The application should run on a wide variety of platforms so as to cater to most of the sections of mobile phone users.

* Since the target devices are mobile phones with limited memory, screen size and processor speed, the application must be able to work successfully using minimal resources.

# System Design

**Voice Chat Application**

The interaction of the client with the server is explained as follows:

* The Mobile Client Application connects to the server and gets registered itself. Both mobile clients should be connected to the server.

* After registration of both the clients, they record voice and send to the server. The Server routes to the intended mobile client.

* The client receives the voice data and buffers it.

* After buffering the voice data the client payout the voice.

* And the steps 2 to 4 repeats further until the call is disconnected by either of the two clients.

**Voice Mail Application**

The interaction of the client with the server is explained as follows:

* The Mobile Client Application connects to the server and gets registereditself. Now both mobile clients need not be connected to the server.

* After registration of the client, it records voice and send to the server.

* The Server stores the received voice data in a file.

* Voice Mail Application Contd.

* The client requests for the voice mail.

* The server sends the file to the client over TCP.

* And the client plays the received voice mail.

**Virtual Class Room Application**

The interaction of the client with the server is explained as follows:

* The Mobile Clients (1 Teacher and many students) Application connects to the server and gets registered. 

* After registration of all the clients, the teacher client application records voice and sends to the server. The Server routes to the intended student mobile clients.

* The student mobile clients receive the voice data and buffer it.

* After buffering the voice data the clients play out the voice.

* The teacher client continuously sends voice data to the student clients until the teacher mobile client stops the voice transmission.
