package com.solargreen.app.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.solargreen.app.R;
import com.solargreen.app.data.ProductRepository;
import com.solargreen.app.models.Product;
import com.solargreen.app.ui.home.HeroAdapter;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment implements ProductAdapter.Listener, HeroAdapter.OnHeroClickListener {

    private static final String ARG_CATEGORY = "category";

    public static CatalogFragment newInstance(String category) {
        CatalogFragment fragment = new CatalogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String category = getArguments() != null ? getArguments().getString(ARG_CATEGORY) : null;
        List<Product> allProducts = ProductRepository.load(requireContext());
        List<Product> filteredProducts = new ArrayList<>(allProducts);

        if (category != null && !category.isEmpty()) {
            filteredProducts.removeIf(p -> !p.category.equalsIgnoreCase(category));
        }

        // Setup Hero Pager Banner
        ViewPager2 heroPager = view.findViewById(R.id.hero_view_pager);
        if (heroPager != null) {
            List<Product> featured = allProducts.subList(0, Math.min(allProducts.size(), 4));
            HeroAdapter heroAdapter = new HeroAdapter(featured, this);
            heroPager.setAdapter(heroAdapter);
        }

        // Setup Product Grid
        RecyclerView recycler = view.findViewById(R.id.product_list);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ProductAdapter adapter = new ProductAdapter(filteredProducts, this);
        recycler.setAdapter(adapter);
    }

    @Override
    public void onProductClick(Product product) {
        openDetail(product);
    }

    @Override
    public void onHeroClick(Product product) {
        openDetail(product);
    }

    private void openDetail(Product product) {
        if (product == null) return;
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.id);
        startActivity(intent);
    }
}