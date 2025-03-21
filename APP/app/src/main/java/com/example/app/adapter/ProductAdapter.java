package com.example.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.Model.Product;
import com.example.app.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    //22110458 - Nguyễn Hữu Vinh
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, null);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set tên danh mục
        holder.txtProductName.setText(product.getProductName());

        // Load ảnh từ URL vào ImageView bằng Glide
        Glide.with(context)
                .load(product.getImages())
                .placeholder(R.drawable.ic_launcher_foreground) // Hình ảnh mặc định nếu không có ảnh
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName;
        ImageView imgProduct;
        Context context;
        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            txtProductName = itemView.findViewById(R.id.tvNameProduct);
            imgProduct = itemView.findViewById(R.id.productImage);

            // Bắt sự kiện click cho item
            itemView.setOnClickListener(view ->
                    Toast.makeText(context, "Bạn đã chọn category: " + txtProductName.getText().toString(), Toast.LENGTH_SHORT).show()
            );
        }
    }
}
