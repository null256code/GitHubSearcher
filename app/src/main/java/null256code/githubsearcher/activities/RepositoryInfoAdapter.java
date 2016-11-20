package null256code.githubsearcher.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import null256code.githubsearcher.R;

/**
 * リポジトリ情報をListViewに表示するときのAdapter
 * アカウント画像を取得する処理はPicassoでやっている。
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
    public RepositoryInfo getItem(int i) {
        return repositoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return repositoryList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.repository_row,viewGroup,false);

        ((TextView)view.findViewById(R.id.ownerLogin)).setText(repositoryList.get(i).getOwnerLogin());
        ((TextView)view.findViewById(R.id.repositoryName)).setText(repositoryList.get(i).getFullName());
        ((TextView)view.findViewById(R.id.repositoryURL)).setText(repositoryList.get(i).getHtmlURL());
        ((TextView)view.findViewById(R.id.language)).setText(repositoryList.get(i).getLanguage());
        ((TextView)view.findViewById(R.id.description)).setText(repositoryList.get(i).getDescription());

        String avatarUrl = repositoryList.get(i).getOwnerImgUrl();
        if (StringUtils.isNotBlank(avatarUrl)) {
            ImageView imageView = (ImageView)view.findViewById(R.id.ownerAvatar);
            Picasso.with(context).load(avatarUrl).into(imageView);
        }
        return view;
    }
}
