# Shared-Whiteboard
This project designs and implements a shared whiteboard, allowing multiple users to draw simultaneously on a canvas. Users can use a pen, eraser, text tool and draw basic shapes, such as lines, circles, ovals, rectangles.
The communication between server and users is based on remote interfaces.
This project is based on Java RMI Remote interface; each function in it should throw RemoteException. Any object
that is remote must implement this interface. Only those methods specified in a ”remote interface” are available remotely.
Once the first user opens the GUI, he will be automatically connected to the RMI agency and become the manager.

# Components
The main functionalities of whiteboard are the followings.
**Draw and Text**
Whiteboard interface has tool buttons, which contains “Line”, “Circle”, “Rectangle”, “Oval”, “Pen”, “Eraser”, “Text”.
Users can draw lines, circles, ovals, rectangles in the whiteboard, and use pen and eraser to paint freely. They also can
insert a text anywhere on the drawing board. I implement these drawing functions based on Java2D drawing package to
draw a certain shape. And I implement MouseListene, MouseMotionListener interfaces to monitor mouse movement
and click or drag position coordinates.
![manager](https://user-images.githubusercontent.com/62585203/131211159-7e1ebf71-8c76-4e54-b068-b3456946d885.png)
![user](https://user-images.githubusercontent.com/62585203/131211164-3b58e9a8-c722-4764-b4ba-1aa5cb0b0264.png)

**Choose a color to draw**
User can choose their favorite color to draw the above shapes. The default color at the beginning is black. A simple
color palette is on the top of canvas and contains standard colors, such as blue, yellow, pink, green and red. Besides, the
user can click on “More colors” button to open a complete palette, which contains more than 128 colors.
![color chooser](https://user-images.githubusercontent.com/62585203/131211093-f1363d0e-14b1-4f63-8afe-149759212d22.png)

**Same user cannot enter the same whiteboard room**
In the project, I enforce unique usernames. When a user wants to join the whiteboard, I must check if his username
has already existed in the whiteboard. If it has already existed, a pop-up window will remind the user that you need to
change another username and try again.

**Seek approval from manager**
![approval](https://user-images.githubusercontent.com/62585203/131211116-25c78ee7-2739-4ee9-b547-bfdf82b64e0c.png)
**Chat Window**
On left side of GUI, there is a chat room to help all users communicate more conveniently. A notification will be displayed
in the chat room when there is a change in the current online users, such as someone leaves the board, someone is kicked
out by the manager, someone enter the board. Besides, all online users can type texts into the chat room in real-time.
![chat](https://user-images.githubusercontent.com/62585203/131211172-24fa1025-ed0f-41c4-9abb-d85bbcdd77c3.png)

**“Manager management**
There is a “File” button on the top of the GUI on the manager side. It implements functions about managing
the board information, such as open, save, save as, new and close the whiteboard. It is important to note that only the
manager has the authority to perform these operations. I use a Boolean variable to identify whether the current client is
the manager.
![File](https://user-images.githubusercontent.com/62585203/131211190-b7190eb9-2f0b-4bc9-b3cf-5f20c793faaf.png)

**Kick out**
Only the manager has the right to
kick out a user. Because each user has a unique username, the manager can type the user’s username he wants to kick
out to force the user to exit the board.

![kick1](https://user-images.githubusercontent.com/62585203/131211187-1107fa64-ca25-4a31-af15-21c52010aaa8.png)
![kick2](https://user-images.githubusercontent.com/62585203/131211200-467a0b20-cc33-4dcf-a229-789f6966435d.png)

**User interactions**
 In this system, I use some pop-up windows to inform users errors or
instructions. A pop-up window will appear to inform user to change another name when his name exists in the board.
There are also pop-up windows during system operation. For instance, the manager rejects the request to enter the board;
a user is kicked out by the manager; the room is already created.
Besides, I use chat room to interact with all users. When there is a change in the current active users, a message from
server will inform all users. Figure below shows the chat window when users enter.
![inter](https://user-images.githubusercontent.com/62585203/131211222-2edeaed3-1fc7-4b3e-af1c-2f98b2335afd.png)


**Display online usernames**
**Synchronize the drawing board state**
Users may connect and disconnect at any time. When a user joins the board, he will obtain the current state of the
whiteboard. Thus, all active users can use drawing tools synchronously without delays.

# UML class diagram


![board](https://user-images.githubusercontent.com/62585203/131211135-058fcdee-fb26-4433-98f5-1465a70f7c6b.jpg)
