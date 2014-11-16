package com.oissela.software.sampleapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.oissela.software.sampleapp.model.MyComment;
import com.oissela.software.sampleapp.model.MyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * An example that shows how to use MultiLevelExpIndListAdapter to display some content at
 * the top of a list (in this case a lorem ipsum string) and some comments at the bottom.
 */
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

    public static class ListViewFragment extends Fragment {
        private static final String GROUPS_KEY = "groups_key";

        private MyAdapter mAdapter;
        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            // code copied from https://developer.android.com/training/material/lists-cards.html
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.content_recyclerview);

            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new MyAdapter(getActivity(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mRecyclerView.getChildPosition(v);
                    mAdapter.toggleGroup(position);
                }
            });
            mRecyclerView.setAdapter(mAdapter);

            MyContent content = getDummyContent();
            mAdapter.add(content);
            List<MyComment> comments = getDummyComments();
            mAdapter.addAll(comments);

            if (savedInstanceState != null) {
                List<Integer> groups = savedInstanceState.getIntegerArrayList(GROUPS_KEY);
                mAdapter.restoreGroups(groups);
            }

            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putIntegerArrayList(GROUPS_KEY, mAdapter.saveGroups());
            super.onSaveInstanceState(outState);
        }

        private MyContent getDummyContent() {
            return new MyContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam sed odio scelerisque, condimentum neque non, venenatis neque. Mauris nec feugiat felis, id porta nibh. In hac habitasse platea dictumst. Phasellus egestas rutrum justo, sit amet pharetra nulla egestas non. Vivamus ultricies ligula id mauris viverra, mattis volutpat turpis hendrerit. In hac habitasse platea dictumst. Nulla congue, lorem eu placerat luctus, metus lacus convallis est, non porta tellus nisi ut neque. Pellentesque posuere gravida tincidunt. Maecenas aliquet, nulla id vestibulum elementum, enim leo mattis ipsum, in lobortis quam enim pretium justo.");
        }

        private List<MyComment> getDummyComments() {
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
    }
}
