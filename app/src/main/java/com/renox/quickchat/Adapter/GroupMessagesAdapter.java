package com.renox.quickchat.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.renox.quickchat.Model.Message;
import com.renox.quickchat.R;
import com.renox.quickchat.databinding.DeleteDialogBinding;
import com.renox.quickchat.databinding.ItemReceivedBinding;
import com.renox.quickchat.databinding.ItemSendBinding;

import java.util.ArrayList;

public class GroupMessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;
    final int ITEM_SENT = 1;
    final  int ITEM_RECEIVED = 2;



    public GroupMessagesAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.item_send, parent, false);
            return new SentViewHolder(view);
        } else {
            View view =LayoutInflater.from(context).inflate(R.layout.item_received, parent, false);
            return  new ReceiveViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getUid().equals(message.getSenderId())){
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVED;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

//        int[] reactions = new int[]{
//                R.drawable.ic_fb_like,
//                R.drawable.ic_fb_love,
//                R.drawable.ic_fb_laugh,
//                R.drawable.ic_fb_wow,
//                R.drawable.ic_fb_sad,
//                R.drawable.ic_fb_angry
//        };
//
//        ReactionsConfig config = new ReactionsConfigBuilder(context)
//                .withReactions(reactions)
//                .build();
//
//        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
//
//            if (holder.getClass() == SentViewHolder.class) {
//                SentViewHolder viewHolder = (SentViewHolder) holder;
//                viewHolder.binding.feeling.setImageResource(reactions[pos]);
//                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
//            } else {
//                ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
//                viewHolder.binding.feeling.setImageResource(reactions[pos]);
//                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
//            }
//
//            message.setFeeling(pos);
//
//            FirebaseDatabase.getInstance().getReference()
//                    .child("chats")
//                    .child(senderRoom)
//                    .child("messages")
//                    .child(message.getMessageId()).setValue(message);
//
//            FirebaseDatabase.getInstance().getReference()
//                    .child("chats")
//                    .child(receiverRoom)
//                    .child("messages")
//                    .child(message.getMessageId()).setValue(message);
//
//            return true; // true is closing popup, false is requesting a new selection
//        });

        if (holder.getClass() == SentViewHolder.class){
            SentViewHolder viewHolder = (SentViewHolder) holder;

            if (message.getMessage().equals("photo")){
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context).load(message.getImageUrl())
                        .placeholder(R.drawable.place_holder)
                        .into(viewHolder.binding.image);
            }
            viewHolder.binding.message.setText(message.getMessage());

//            if (message.getFeeling() >= 0){
//                message.setFeeling(reactions[ (int) message.getFeeling()]);
//                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
//            }else {
//                viewHolder.binding.feeling.setVisibility(View.GONE);
//            }
//            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popup.onTouch(v, event);
//                    return false;
//                }
//            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog,null);
                    DeleteDialogBinding binding = DeleteDialogBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();

                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.setMessage("This message is removed.");
//                            message.setFeeling(-1);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("public")
                                    .child(message.getMessageId()).setValue(message);

                            dialog.dismiss();
                        }
                    });

                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("public")
                                    .child(message.getMessageId()).setValue(null);
                            dialog.dismiss();
                        }
                    });

                    binding.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    return false;
                }
            });
        } else {
            GroupMessagesAdapter.ReceiveViewHolder viewHolder = (GroupMessagesAdapter.ReceiveViewHolder) holder;
            if (message.getMessage().equals("photo")){
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context).load(message.getImageUrl()).placeholder(R.drawable.place_holder).into(viewHolder.binding.image);
            }
            viewHolder.binding.message.setText(message.getMessage());

//            if (message.getFeeling() >= 0){
//                //message.setFeeling(reactions[(int) message.getFeeling()]);
//                viewHolder.binding.feeling.setImageResource(reactions[message.getFeeling()]);
//                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
//            }else {
//                viewHolder.binding.feeling.setVisibility(View.GONE);
//            }
//
//            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popup.onTouch(v, event);
//                    return false;
//                }
//            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
                    DeleteDialogBinding binding = DeleteDialogBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();

                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.setMessage("This message is removed.");
                            message.setFeeling(-1);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("public")
                                    .child(message.getMessageId()).setValue(message);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("public")
                                    .child(message.getMessageId()).setValue(message);
                            dialog.dismiss();
                        }
                    });

                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("public")
                                    .child(message.getMessageId()).setValue(null);
                            dialog.dismiss();
                        }
                    });

                    binding.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    return false;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        ItemSendBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSendBinding.bind(itemView);
        }
    }

    public class ReceiveViewHolder extends RecyclerView.ViewHolder {

        ItemReceivedBinding binding;
        public ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceivedBinding.bind(itemView);
        }
    }
}
