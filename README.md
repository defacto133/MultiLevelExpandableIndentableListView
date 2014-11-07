# MultiLevelExpandableIndentableListView

This library display hierarchical data (e.g. comments) in a list view, so that the user can collapse and expand elements. Every element is indented accordingly to its level in the hierarchy.

Here's a video of the SampleApp demo

[![Screenshot of the demo](http://img.youtube.com/vi/dweRJ4Ukb0Q/0.jpg)](http://www.youtube.com/watch?v=dweRJ4Ukb0Q)

### How to import the library in your probject

1. Clone the repository

   `git clone https://github.com/defacto133/MultiLevelExpandableIndentableListView.git`

2. Open the Module Settings (press F4)
3. Click on the top left green cross to add a new module
4. Select "Import Existing Project"
5. As Source Directory select the directory where you cloned the repository
6. The module :multilevelexpindlistview contains the library so you have to import this. The module :sampleapp is optional and it's a simple example of how to use the library.
7. Click Finish
8. Now in the Modules listing you see a new library module multilevelexpindlistview (and a project module sampleapp if you decided to import that too). In the Modules listing select your project module (usually is called app) and click on Dependencies.
9. Click on the top right green cross and select Module dependency
10. Select :multilevelexpindlistview 
