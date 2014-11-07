package com.oissela.software.multilevelexpindlistview;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Multi-level expandable indentable list adapter.
 * Initially all elements in the list are single items. When you click on an item, all its
 * descendants are collapsed and the item clicked becomes a group. When you click on a group,
 * the descendants are shown again, but groups inside other groups are kept collapsed.
 *
 * The way the data is put in the item/group views is similar to the method used by SimpleAdapter,
 * but in this case there are 2 views.
 *
 * Data and the groups are static fields so that state is preserved when a configuration
 * change happens (e.g. screen rotation).
 */
public class MultiLevelExpIndListAdapter extends BaseAdapter  {
    /**
     * View type of a single item.
     */
    public static final int VIEW_TYPE_ITEM = 0;
    /**
     * View type of a group of items and/or groups.
     */
    public static final int VIEW_TYPE_GROUP = 1;

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
     * {@link #sData} is modified.
     */
    private boolean mNotifyOnChange = true;

    private final Context mContext;
    /**
     * Layout of the items.
     */
    private final int mResourceItem;
    /**
     * Layout of the groups.
     */
    private final int mResourceGroup;
    /**
     * Map an index i to a key k.
     */
    private final String[] mFromI;
    /**
     * Map an index i to a resource id of a View contained in an item view.
     */
    private final int[] mToI;
    /**
     * Map an index i to a key k.
     */
    private final String[] mFromG;
    /**
     * Map an index i to a resource id of a View contained in a group view.
     */
    private final int[] mToG;
    /**
     * List of items.
     */
    private static List<ExpIndData> sData;
    /**
     * Map an item to the relative group.
     * e.g.: if the user click on item 6 then mGroups(item(6)) = {all items/groups below item 6}
     */
    private static Map<ExpIndData, List<? extends ExpIndData>> sGroups;
    private final LayoutInflater mInflater;
    /**
     * View documentation for MultiLevelExpIndListAdapter.ViewBinder
     */
    private ViewBinder mItemViewBinder;
    private ViewBinder mGroupViewBinder;
    /**
     * Left padding unit. e.g.: Item with indentation 2 has mPaddingDP * 2 space on the left
     */
    private int mPaddingDP = 10;

    /**
     * Interface that every item has to implements.
     */
    public interface ExpIndData {
        /**
         * @return The children of this item.
         */
        List<? extends ExpIndData> getChildren();

        /**
         * @return True if this item is a group.
         */
        boolean isGroup();

        /**
         * @param value True if this item is a group
         */
        void setIsGroup(boolean value);

        /**
         * @return A map that associate to every key k (they keys are in the array fromI/fromG)
         *         some object. If this object is a string, or can be cast into a string, it
         *         will be show in the relative resource id (the resource ids are in the array toI/toG).
         *         If the object is complex use ViewBinder.
         */
        Map<String, ?> getData();

        /**
         * @param groupSize Set the number of items in the group.
         *                  Note: groups contained in other groups are counted just as one, not
         *                  as the number of items that they contain.
         */
        void setGroupSize(int groupSize);

        /**
         * @return The level of indentation
         */
        int getIndentation();
    }

    /**
     * This class can be used by external clients of MultiLevelExpIndListAdapter to bind
     * values to views.
     *
     * You should use this class to bind values to views that are not
     * directly supported by MultiLevelExpIndListAdapter or to change the way binding
     * occurs for views supported by MultiLevelExpIndListAdapter.
     */
    public static interface ViewBinder {
        /**
         * Binds the specified data to the specified view.
         *
         * When binding is handled by this ViewBinder, this method must return true.
         * If this method returns false, MultiLevelExpIndListAdapter will attempts to handle
         * the binding on its own.
         *
         * @param view the view to bind the data to
         * @param data the data to bind to the view
         * @param textRepresentation a safe String representation of the supplied data:
         * it is either the result of data.toString() or an empty String but it
         * is never null
         *
         * @return true if the data was bound to the view, false otherwise
         */
        boolean setViewValue(View view, Object data, String textRepresentation);
    }

    /**
     *
     * @param context The current context.
     * @param resourceItem Resource identifier of a view layout that defines the views for
     *                     this list items. The layout file should include at least
     *                     those named views defined in "toI"
     * @param fromI Map an index i to a key k.
     * @param toI Map an index i to a resource id of a View contained in an item view.
     * @param resourceGroup Resource identifier of a view layout that defines the views for
     *                     this list groups. The layout file should include at least
     *                     those named views defined in "toG"
     * @param fromG Map an index i to a key k.
     * @param toG Map an index i to a resource id of a View contained in a group view.
     */
    public MultiLevelExpIndListAdapter(Context context,
                                       int resourceItem, String[] fromI, int[] toI,
                                       int resourceGroup, String[] fromG, int[] toG) {
        mContext = context;
        mResourceItem = resourceItem;
        mFromI = fromI;
        mToI = toI;
        mResourceGroup = resourceGroup;
        mFromG = fromG;
        mToG = toG;

        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (sGroups == null)
            sGroups = new HashMap<ExpIndData, List<? extends ExpIndData>>();
        if (sData == null)
            sData = new ArrayList<ExpIndData>();
    }

    /**
     * Item that has indentation = n, has n * paddingDP space on the left
     * @param paddingDP The left padding base unit value in dp
     */
    public void setPaddingDP(int paddingDP) {
        mPaddingDP = paddingDP;
    }

    public void add(ExpIndData item) {
        sData.add(item);
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(Collection<? extends ExpIndData> data) {
        sData.addAll(data);
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void clear() {
        sData.clear();
        sGroups.clear();
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void insert(ExpIndData item, int index) {
        sData.add(index, item);
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void remove(ExpIndData item) {
        sData.remove(item);
        if (sGroups.containsKey(item))
            sGroups.remove(item);
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sData.size();
    }

    @Override
    public ExpIndData getItem(int i) {
        return sData.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isGroup() ? VIEW_TYPE_GROUP : VIEW_TYPE_ITEM;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            int resource;
            switch (getItemViewType(i)) {
                case VIEW_TYPE_ITEM:
                    resource = mResourceItem;
                    break;
                case VIEW_TYPE_GROUP:
                    resource = mResourceGroup;
                    break;
                default:
                    throw new IllegalStateException("unkown view type");
            }
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        switch (getItemViewType(i)) {
            case VIEW_TYPE_ITEM:
                bindView(i, view, mFromI, mToI, getItemViewBinder());
                break;
            case VIEW_TYPE_GROUP:
                bindView(i, view, mFromG, mToG, getGroupViewBinder());
                break;
            default:
                throw new IllegalStateException("unkown view type");
        }

        view.setPadding(getPaddingPixels(mPaddingDP) * getItem(i).getIndentation(),0,0,0);
        return view;
    }

    private int getPaddingPixels(int mPaddingDP) {
        Resources r = mContext.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mPaddingDP, r.getDisplayMetrics());
    }

    private void bindView(int position, View view, String[] from, int[] to, ViewBinder binder) {
        final Map dataSet = sData.get(position).getData();
        if (dataSet == null) {
            return;
        }
        final int count = to.length;
        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                final Object data = dataSet.get(from[i]);
                String text = data == null ? "" : data.toString();
                if (text == null) {
                    text = "";
                }
                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, data, text);
                }
                if (!bound) {
                    if (v instanceof Checkable) {
                        if (data instanceof Boolean) {
                            ((Checkable) v).setChecked((Boolean) data);
                        } else if (v instanceof TextView) {
                            // Note: keep the instanceof TextView check at the bottom of these
                            // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                            setViewText((TextView) v, text);
                        } else {
                            throw new IllegalStateException(v.getClass().getName() +
                                    " should be bound to a Boolean, not a " +
                                    (data == null ? "<unknown type>" : data.getClass()));
                        }
                    } else if (v instanceof TextView) {
                        // Note: keep the instanceof TextView check at the bottom of these
                        // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        if (data instanceof Integer) {
                            setViewImage((ImageView) v, (Integer) data);
                        } else {
                            setViewImage((ImageView) v, text);
                        }
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this SimpleAdapter");
                    }
                }
            } else {
                throw new IllegalStateException("shouldn't be null");
            }
        }
    }

    /**
     * Control whether methods that change the list ({@link #add},
     * {@link #insert}, {@link #remove}, {@link #clear}) automatically call
     * {@link #notifyDataSetChanged}. If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     *
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     * automatically call {@link
     * #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    /**
     * Returns the {@link ViewBinder} used to bind data to item views.
     *
     * @return a ViewBinder or null if the binder does not exist
     */
    public ViewBinder getItemViewBinder() {
        return mItemViewBinder;
    }

    /**
     * Sets the binder used to bind data to group views.
     *
     * @param viewBinder the binder used to bind data to group views, can be null to
     * remove the existing binder
     */
    public void setGroupViewBinder(ViewBinder viewBinder) {
        mGroupViewBinder = viewBinder;
    }

    /**
     * Returns the {@link ViewBinder} used to bind data to group views.
     *
     * @return a ViewBinder or null if the binder does not exist
     */
    public ViewBinder getGroupViewBinder() {
        return mGroupViewBinder;
    }

    /**
     * Sets the binder used to bind data to item views.
     *
     * @param viewBinder the binder used to bind data to item views, can be null to
     * remove the existing binder
     */
    public void setItemViewBinder(ViewBinder viewBinder) {
        mItemViewBinder = viewBinder;
    }


    /**
     * Called by bindView() to set the text for a TextView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to a TextView.
     *
     * @param v TextView to receive text
     * @param text the text to be set for the TextView
     */
    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

    /**
     * Called by bindView() to set the image for an ImageView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to an ImageView.
     *
     * This method is called instead of {@link #setViewImage(ImageView, String)}
     * if the supplied data is an int or Integer.
     *
     * @param v ImageView to receive an image
     * @param value the value retrieved from the data set
     *
     * @see #setViewImage(ImageView, String)
     */
    public void setViewImage(ImageView v, int value) {
        v.setImageResource(value);
    }

    /**
     * Called by bindView() to set the image for an ImageView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to an ImageView.
     *
     * By default, the value will be treated as an image resource. If the
     * value cannot be used as an image resource, the value is used as an
     * image Uri.
     *
     * This method is called instead of {@link #setViewImage(ImageView, int)}
     * if the supplied data is not an int or Integer.
     *
     * @param v ImageView to receive an image
     * @param value the value retrieved from the data set
     *
     * @see #setViewImage(ImageView, int)
     */
    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    /**
     * Expand the group at position "posititon".
     * @param position The position of the group that has to expanded
     */
    public void expandGroup(int position) {
        ExpIndData firstItem = getItem(position);

        if (!firstItem.isGroup()) {
            return;
        }

        // get the group of the descendants of firstItem
        List<? extends ExpIndData> group = sGroups.remove(firstItem);

        sData.addAll(position + 1, group);

        firstItem.setIsGroup(false);
        firstItem.setGroupSize(0);

        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Collapse the descendants of the item at position "position".
     * @param position The position of the element that has to collapsed
     */
    public void collapseGroup(int position) {
        ExpIndData firstItem = getItem(position);

        if (firstItem.getChildren() == null || firstItem.getChildren().isEmpty())
            return;

        // group containing all the descendants of firstItem
        List<ExpIndData> group = new ArrayList<ExpIndData>();
        // stack for depth first search
        List<ExpIndData> stack = new ArrayList<ExpIndData>();
        int groupSize = 0;

        stack.addAll(firstItem.getChildren());

        while (!stack.isEmpty()) {
            ExpIndData item = stack.remove(stack.size() - 1);
            group.add(item);
            groupSize++;
            // stop when the item is a leaf or a group
            if (item.getChildren() != null && !item.getChildren().isEmpty() && !item.isGroup()) {
                for (ExpIndData i : item.getChildren()) {
                    stack.add(i);
                }
            }

            sData.remove(item);
        }

        sGroups.put(firstItem, group);
        firstItem.setIsGroup(true);
        firstItem.setGroupSize(groupSize);

        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * Collpase/expand the item at position "position"
     */
    public void toggleGroup(int position) {
        if (getItem(position).isGroup()){
            expandGroup(position);
        } else {
            collapseGroup(position);
        }
    }
}