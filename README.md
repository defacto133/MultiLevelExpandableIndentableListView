# MultiLevelExpandableIndentableListView
 
This component is an adapter that can handle hierarchical data (e.g. comments) in a `RecyclerView`, so that the user can collapse and expand elements inside the hierarchy.

Here's a video of the SampleApp demo

[![Screenshot of the demo](http://img.youtube.com/vi/vZoNEck1uJk/0.jpg)](http://youtu.be/vZoNEck1uJk)

### How to import the library in your probject

There are three ways to import the library. The easiest way is this:

1. Clone the repository

   `$ git clone https://github.com/defacto133/MultiLevelExpandableIndentableListView.git`
   
2. Add these two lines to your `settings.gradle` file

   `include ':multilevelexpindlistview'`
   
   `project(':multilevelexpindlistview').projectDir = new File('<path-to-cloned-repo>/MultiLevelExpandableIndentableListView/multilevelexpindlistview')`

An alternative way is this :

1. Clone the repository

   `$ git clone https://github.com/defacto133/MultiLevelExpandableIndentableListView.git`

2. In Android Studio open the Module Settings (press F4)
3. Click on the top left green cross to add a new module
4. Select "Import Existing Project"
5. As Source Directory select the directory where you cloned the repository
6. The module :multilevelexpindlistview contains the library so you have to import this. The module :sampleapp is optional and it's a simple example of how to use the library.
7. Click Finish
8. Now in the Modules listing you see a new library module multilevelexpindlistview (and a project module sampleapp if you decided to import that too). In the Modules listing select your project module (usually is called app) and click on Dependencies.
9. Click on the top right green cross and select Module dependency
10. Select :multilevelexpindlistview

If you just want to import the aar file:

1. Clone the repository

   `$ git clone https://github.com/defacto133/MultiLevelExpandableIndentableListView.git`

2. cd to the direcotry created

   `$ cd MultiLevelExpandableIndentableListView/`
   
3. Set a variable with the path to the Android SDK

   `$ export ANDROID_HOME=<path-to-andoid-sdk>`

4. Build the project

    `$ ./gradlew build`

    This will make multilevelexpindlistview-release.aar in
    
    \<path-to-cloned-repo\>/MultiLevelExpandableIndentableListView/multilevelexpindlistview/build/outputs/aar/

5. In Android Studio open the Module Settings (press F4)
6. Click on the top left green arrow to add an new module
7. Select "Import .JAR or .AAR Package" and select the .aar file from step 4
8. Now in the Modules listing you see a new module multilevelexpindlistview. In the Modules listing select your project module (usually is called app) and click on Dependencies.
9. Click on the top right green cross and select Module dependency
10. Select :multilevelexpindlistview

### Usage

You should extend the abstract class `MultiLevelExpIndListAdapter` (see `MyAdapter.java` in `sampleapp` for an example) and then associate your adapter with a `RecyclerViw`.
The data that you pass to your `MultiLevelExpIndListAdapter` should implement the interface `MultiLevelExpIndListAdapter.ExpIndData` (see `MyComment.java` in `sampleapp` for an example).
