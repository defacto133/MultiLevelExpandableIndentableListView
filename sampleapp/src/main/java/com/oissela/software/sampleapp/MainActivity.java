package com.oissela.software.sampleapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.oissela.software.multilevelexpindlistview.MultiLevelExpIndListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ListViewFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * An example that shows how to use MultiLevelExpIndListAdapter to display some comments.
     *
     * The item view (listview_item.xml) and group view (listview_group.xml) have both an author
     * and comment field and a vertical colored bar to show the indentation.
     * The group view has on the top right a field that shows the number of items in the groups.
     */
    public static class ListViewFragment extends Fragment {
        // indexes
        private static final int ITEM_AUTHOR = 0;
        private static final int ITEM_COMMENT = 1;
        private static final int ITEM_INDENT_COLOR = 2;
        // keys
        private static final String ITEM_AUTHOR_KEY = "item_author_key";
        private static final String ITEM_COMMENT_KEY = "item_comment_key";
        private static final String ITEM_INDENT_COLOR_KEY = "item_ind_color_key";

        private static final String[] fromI = {ITEM_AUTHOR_KEY, ITEM_COMMENT_KEY, ITEM_INDENT_COLOR_KEY};
        private static final int[] toI = {R.id.author_textview, R.id.comment_textview, R.id.color_band};

        private static final String[] indColors = {"#000000", "#3366FF", "#E65CE6",
                "#E68A5C", "#00E68A", "#CCCC33"};

        // indexes
        private static final int GROUP_AUTHOR = 0;
        private static final int GROUP_COMMENT = 1;
        private static final int GROUP_INDENT_COLOR = 2;
        private static final int GROUP_HIDDEN_CNT = 3;
        // keys
        private static final String GROUP_AUTHOR_KEY = "group_author_key";
        private static final String GROUP_COMMENT_KEY = "group_comment_key";
        private static final String GROUP_INDENT_COLOR_KEY = "group_ind_color_key";
        private static final String GROUP_HIDDEN_CNT_KEY = "group_hidden_cnt_key";

        private static final String[] fromG = {GROUP_AUTHOR_KEY, GROUP_COMMENT_KEY,
                GROUP_INDENT_COLOR_KEY, GROUP_HIDDEN_CNT_KEY};
        private static final int[] toG = {R.id.author_g_textview, R.id.comment_g_textview,
                R.id.g_color_band, R.id.hidden_comments_count_textview};

        private static final String ADAPTER_PARCEL_KEY = "adapter_parcel_key";

        private MultiLevelExpIndListAdapter mAdapter;
        private ListView mListView;

        /**
         * In this example comments are saved using a static field. A better solution would be
         * to make the class that contains the data to implement Parcelable and save the data
         * in the Bundle in onSaveInstanceState.
         */
        private static List<MyComment> sComments;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            mListView = (ListView) rootView.findViewById(R.id.content_listview);

            // item view
            int resourceItem = R.layout.listview_item;
            // group view
            int resourceGroup = R.layout.listview_group;

            mAdapter = new MultiLevelExpIndListAdapter(getActivity(),
                    resourceItem, fromI, toI,
                    resourceGroup, fromG, toG);

            if (savedInstanceState == null) {
                sComments = getDummyData();
                mAdapter.addAll(sComments);
            } else {
                mAdapter.addAll(sComments);
                ArrayList<Integer> groups = savedInstanceState.getIntegerArrayList(ADAPTER_PARCEL_KEY);
                mAdapter.restoreGroups(groups);
            }

            // the author and comment data are simple strings that can be displayed directly,
            // the data is a string that represents a color and so has to be parsed in this ViewBinder
            MultiLevelExpIndListAdapter.ViewBinder vb = new MultiLevelExpIndListAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view.getId() == R.id.color_band || view.getId() == R.id.g_color_band) {
                        String color = (String) data;
                        view.setBackgroundColor(Color.parseColor(color));
                        return true;
                    }
                    return false;
                }
            };
            mAdapter.setItemViewBinder(vb);
            mAdapter.setGroupViewBinder(vb);

            mListView.setAdapter(mAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                    mAdapter.toggleGroup(i);
                }
            });
            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putIntegerArrayList(ADAPTER_PARCEL_KEY, mAdapter.saveGroups());
        }

        private List<MyComment> getDummyData() {
            List<MyComment> comments = new ArrayList<MyComment>();
            comments.add(new MyComment("poopsliquid", "You'd think that after he was pulled on the ground by the machine he might take a break and reevaluate his choices."));
            comments.add(new MyComment("Nizzler", "that's what a skinny little weakling would do. You a skinny little weakling, /u/poopsliquid?"));
            comments.get(0).addChild(comments.get(1));
            comments.add(new MyComment("banedes", "Hell yeah get him man. We don't need no more thinks at the gym."));
            comments.get(1).addChild(comments.get(2));
            comments.add(new MyComment("Nizzler", "What a scrawny little twerp."));
            comments.get(2).addChild(comments.get(3));
            comments.add(new MyComment("Daibhead", "When the machine starts working out on you its time to try something else."));
            comments.get(0).addChild(comments.get(4));
            comments.add(new MyComment("bigtfatty", "Got to say I'm pretty impressed he held on though and didn't drop (and possibly break) the weights."));
            comments.get(0).addChild(comments.get(5));

            comments.add(new MyComment("wwickeddogg", "Good practice for when you are dragging your dead girlfriends body out of the house with one hand while also trying to keep the dog out of the way with the other hand."));
            comments.add(new MyComment("banedes", "Amen been there brother I have been there."));
            comments.get(6).addChild(comments.get(7));
            comments.add(new MyComment("GratefulGreg89", "I know right haven't we all?"));
            comments.get(7).addChild(comments.get(8));
            comments.add(new MyComment("SoefianB", "I was there yesterday"));
            comments.get(8).addChild(comments.get(9));
            comments.add(new MyComment("Aznleroy", "For me it was ABOUT A WEEK AGO. WEEK AGO."));
            comments.get(9).addChild(comments.get(10));
            comments.add(new MyComment("neurorgasm", "If I remember correctly, everybody was catching bullet holes that day."));
            comments.get(10).addChild(comments.get(11));
            comments.add(new MyComment("HippolyteClio", "I went that long once, never again."));
            comments.get(10).addChild(comments.get(12));
            comments.add(new MyComment("heyYOUguys1", "did you just about a week ago yourself?"));
            comments.get(10).addChild(comments.get(13));
            comments.add(new MyComment("MrMontage", "Exactly, it's a very functional exercise."));
            comments.get(6).addChild(comments.get(14));

            comments.add(new MyComment("CranberryNapalm", "Ah, the Lat Lean-Back Single Plate Hop-Pull. Do you even lift, bros?"));
            comments.add(new MyComment("toke81", "Do you even hop and pull?"));
            comments.get(15).addChild(comments.get(16));
            comments.add(new MyComment("itza_me", "Nothing beats this rowing machine mastery for me."));
            comments.add(new MyComment("All_The_Ragrets", "One... One... One... One... One..."));
            comments.get(17).addChild(comments.get(18));
            comments.add(new MyComment("leeboof", "Not even one..."));
            comments.get(18).addChild(comments.get(19));
            return comments;
        }

        /**
         * Class that represents a comment
         */
        private static class MyComment implements MultiLevelExpIndListAdapter.ExpIndData {
            private int mIndentation;
            private List<MyComment> mChildren;
            private boolean mIsGroup;
            private int mGroupSize;
            private Map<String, String> mData;

            public MyComment(String author, String comment) {
                mChildren = new ArrayList<MyComment>();
                mData = new HashMap<String, String>();

                mData.put(fromI[ITEM_AUTHOR], author);
                mData.put(fromI[ITEM_COMMENT], comment);

                mData.put(fromG[GROUP_AUTHOR], author);
                mData.put(fromG[GROUP_COMMENT], comment);

                setIndentation(0);
            }

            @Override
            public List<? extends MultiLevelExpIndListAdapter.ExpIndData> getChildren() {
                return mChildren;
            }

            @Override
            public boolean isGroup() {
                return mIsGroup;
            }

            @Override
            public void setIsGroup(boolean value) {
                mIsGroup = value;
            }

            @Override
            public int getIndentation() {
                return mIndentation;
            }

            @Override
            public Map<String, ?> getData() {
                return mData;
            }

            @Override
            public void setGroupSize(int groupSize) {
                mGroupSize = groupSize;
                mData.put(fromG[GROUP_HIDDEN_CNT], "+ " + Integer.toString(mGroupSize));
            }

            public void addChild(MyComment child) {
                mChildren.add(child);
                child.setIndentation(getIndentation() + 1);
            }

            private void setIndentation(int indentation) {
                mIndentation = indentation;
                mData.put(fromI[ITEM_INDENT_COLOR], indColors[mIndentation]);
                mData.put(fromG[GROUP_INDENT_COLOR], indColors[mIndentation]);
            }
        }
    }
}
