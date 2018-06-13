package io.github.lucasduete.github_listrep;

import android.app.Activity;
import android.graphics.drawable.Icon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyListAdapter extends BaseAdapter {

    private final List<Repositorio> repositorios;
    private final Activity activity;

    public MyListAdapter(List<Repositorio> repositorios, Activity activity) {
        this.repositorios = repositorios;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return repositorios.size();
    }

    @Override
    public Object getItem(int position) {
        return repositorios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = activity.getLayoutInflater().inflate(R.layout.repos_list, parent, false);
        Repositorio repositorio = repositorios.get(position);

        TextView textView;

        textView = (TextView) view.findViewById(R.id.textViewNameRepo);
        textView.setText(repositorio.getNome());

        textView = (TextView) view.findViewById(R.id.textViewNameAuthor);
        textView.setText(repositorio.getNomeAutor());

        textView = (TextView) view.findViewById(R.id.textViewDescRepo);
        textView.setText(repositorio.getDescricao());

        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewFotoRepo);
        Picasso.get().load(repositorio.getFoto()).into(imageView);

        return view;
    }
}
