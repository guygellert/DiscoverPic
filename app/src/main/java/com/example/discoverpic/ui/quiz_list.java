package com.example.discoverpic.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.discoverpic.databinding.FragmentQuizListBinding;
import com.example.discoverpic.ui.home.HomeViewModel;

public class quiz_list extends Fragment {
    FragmentQuizListBinding binding;
    HomeViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuizListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        QuizRecyclerAdapter adapter = new QuizRecyclerAdapter(getLayoutInflater(),viewModel.getAllPosts().getValue());
        binding.recyclerView.setAdapter(adapter);

        binding.progressBar.setVisibility(View.GONE);

        viewModel.getAllPosts().observe(getViewLifecycleOwner(),list->{
            adapter.setData(list);
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }
}