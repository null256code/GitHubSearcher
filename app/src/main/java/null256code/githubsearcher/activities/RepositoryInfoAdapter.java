package null256code.githubsearcher.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import null256code.githubsearcher.R;

/**
 * Created by kanto on 2016/11/20.
 */

public class RepositoryInfoAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<RepositoryInfo> repositoryList;

    public RepositoryInfoAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRepositoryList(ArrayList<RepositoryInfo> repositoryList) {
        this.repositoryList = repositoryList;
    }

    @Override
    public int getCount() {
        return repositoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return repositoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return repositoryList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.repository_row,viewGroup,false);
        if (repositoryList.get(i).getOwnerImg() != null) {
            ((ImageView)view.findViewById(R.id.ownerAvatar)).setImageBitmap(repositoryList.get(i).getOwnerImg());
        }
        ((TextView)view.findViewById(R.id.ownerLogin)).setText(repositoryList.get(i).getOwnerLogin());
        ((TextView)view.findViewById(R.id.repositoryURL)).setText(repositoryList.get(i).getHtmlURL());
        ((TextView)view.findViewById(R.id.language)).setText(repositoryList.get(i).getLanguage());
        ((TextView)view.findViewById(R.id.description)).setText(repositoryList.get(i).getDescription());
        return view;
    }
}
