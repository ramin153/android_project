package com.example.androidproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.App;
import com.example.androidproject.R;
import com.example.androidproject.emailSender.GMailSender;
import com.example.androidproject.helper.MyToasty;

import java.text.SimpleDateFormat;
import java.util.List;

public class Noteadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private List<NoteData> items;
    private MyOnClick clickDelete;
    private MyOnClick clickAction;





    public Noteadapter(Context context, List<NoteData> items,MyOnClick clickAction, MyOnClick clickDelete) {
        this.context = context;
        this.items = items;
        this.clickAction = clickAction;
        this.clickDelete = clickDelete;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).typeOfNote.getVal();
    }
    //Reminde is link mode
    //action is done by clicking
    //location is location
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if(viewType == TypeOfNote.action())
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclervie_clicknote,parent,false);
            NoteClickHolder viewHolder = new NoteClickHolder(view);
            return viewHolder;
        }else if(viewType == TypeOfNote.email())//email
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_emailaddress,parent,false);
            EmailHolder viewHolder = new EmailHolder(view);
            return viewHolder;
        }else//reminder
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_remindernote,parent,false);
            NoteReminderHolder viewHolder = new NoteReminderHolder(view);
            return viewHolder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final NoteData item= items.get(position);
        SimpleDateFormat format = new SimpleDateFormat("HH : mm");
        if(holder instanceof NoteClickHolder)
        {
            final NoteClickHolder viewHolder = (NoteClickHolder) holder;
            if(item.isClick)
            {
                viewHolder.click.setVisibility(View.VISIBLE);
                viewHolder.outLineClick.setVisibility(View.GONE);
            }
            viewHolder.outLineClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((boolean)clickAction.click((Integer)item.id,holder.getAdapterPosition())[0]) {
                        viewHolder.click.setVisibility(View.VISIBLE);
                        viewHolder.outLineClick.setVisibility(View.GONE);
                    }
                }
            });
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickDelete.click(holder.getAdapterPosition());
                }
            });
            viewHolder.text.setText(item.text);
            viewHolder.title.setText(item.title);
            viewHolder.date.setText(format.format(item.date.getTime()));

        }else if(holder instanceof  NoteReminderHolder)
        {
            NoteReminderHolder viewHolder = (NoteReminderHolder) holder;
            viewHolder.text.setText(item.text);
            viewHolder.title.setText(item.title);
            viewHolder.date.setText(format.format(item.date.getTime()));
            viewHolder.link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = item.url;
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);
                }
            });
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickDelete.click(holder.getAdapterPosition());
                }
            });
        }else if(holder instanceof EmailHolder)
        {
            EmailHolder viewHolder = (EmailHolder) holder;
            viewHolder.text.setText(item.text);
            viewHolder.title.setText(item.title);
            viewHolder.email.setText(item.address);
            viewHolder.date.setText(format.format(item.date.getTime()));

            viewHolder.send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            SharedPreferences prefs = context.getSharedPreferences(App.SHAREDPREFERENCES, context.MODE_PRIVATE);
                            String semail = prefs.getString(App.SHAREDPREFERENCES_EMAIL, "no email");
                            String spassword = prefs.getString(App.SHAREDPREFERENCES_PASSWORD, "no pass");
                            if (semail.equals("no email") || spassword.equals("no pass") )
                            {
                                new MyToasty(context).warning("plz complite date for email in password section",true);
                                return;
                            }
                            try {
                                System.out.println(semail);
                                System.out.println(spassword);
                                System.out.println(item.title);
                                System.out.println( item.text);
                                System.out.println(item.address);

                                GMailSender sender = new GMailSender(semail, spassword);
                                sender.sendMail(item.title,
                                        item.text,
                                        semail,
                                        item.address);

                            } catch (Exception e) {
                                Log.e("SendMail", e.getMessage(), e);
                                e.printStackTrace();
                            }
                        }

                    }).start();
                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickDelete.click(holder.getAdapterPosition());
                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return items.size();
    }




    public static class NoteClickHolder extends  RecyclerView.ViewHolder
    {
        TextView title;
        TextView text;
        TextView date;
        Button delete;
        ImageView click;
        ImageView outLineClick;
        ViewGroup layout;


        public NoteClickHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recyclerclicknote_title);
            text = itemView.findViewById(R.id.recyclerclicknote_text);
            date = itemView.findViewById(R.id.recyclerclicknote_date);
            click = itemView.findViewById(R.id.recyclerclicknote_click);
            outLineClick = itemView.findViewById(R.id.recyclerclicknote_clickOutline);
            layout = itemView.findViewById(R.id.recyclerclicknote_main);
            delete =  itemView.findViewById(R.id.recyclerclicknote_delete);


        }
    }

    public static class NoteReminderHolder extends  RecyclerView.ViewHolder
    {
        TextView title;
        TextView text;
        TextView date;
        Button link ;
        ViewGroup layout;
        Button delete;
        public NoteReminderHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recyclerRemindernote_title);
            text = itemView.findViewById(R.id.recyclerRemindernote_text);
            date = itemView.findViewById(R.id.recyclerRemindernote_date);
            link = itemView.findViewById(R.id.recyclerRemindernote_link);
            layout = itemView.findViewById(R.id.recyclerRemindernote_main);
            delete =  itemView.findViewById(R.id.recyclerRemindernote_delete);


        }
    }


    public static class EmailHolder extends  RecyclerView.ViewHolder
    {
        TextView title;
        TextView text;
        TextView email;
        TextView date;
        Button send ;
        ViewGroup layout;
        Button delete;
        public EmailHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recyclerEmailAddress_title);
            text = itemView.findViewById(R.id.recyclerEmailAddress_text);
            email = itemView.findViewById(R.id.recyclerEmailAddress_address);
            date = itemView.findViewById(R.id.recyclerEmailAddress_date);
            send = itemView.findViewById(R.id.recyclerEmailAddress_send);
            layout = itemView.findViewById(R.id.recyclerEmailAddress_main);
            delete =  itemView.findViewById(R.id.recyclerEmailAddress_delete);


        }
    }
}

