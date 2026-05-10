package com.presenca.primedelivery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.auth.FirebaseAuth;

public class DashboardFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        mAuth = FirebaseAuth.getInstance();

        TextView textNome = view.findViewById(R.id.textNomeUsuario);
        TextView textTipo = view.findViewById(R.id.textTipoUsuario);
        TextView textMensagem = view.findViewById(R.id.textMensagem);
        Button btnFazerPedido = view.findViewById(R.id.btnFazerPedido);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String nome = bundle.getString("nome", "Usuário");
            String tipo = bundle.getString("tipo", "cliente");

            textNome.setText("👋 Olá, " + nome + "!");

            if (tipo.equals("admin")) {
                textTipo.setText("👑 Administrador");
                textMensagem.setText("✅ Firebase Authentication: OK\n✅ Firestore Database: OK\n✅ Sistema funcionando!");
            } else if (tipo.equals("entregador")) {
                textTipo.setText("🛵 Entregador");
                textMensagem.setText("🚚 Entregas Disponíveis\nAguardando pedidos...");
            } else {
                textTipo.setText("👤 Cliente");
                textMensagem.setText("📦 Meus Pedidos\nBem-vindo ao Prime Delivery!");
                btnFazerPedido.setVisibility(View.VISIBLE);
            }
        }

        btnFazerPedido.setOnClickListener(v -> {
            Toast.makeText(getContext(), "🛒 Funcionalidade em desenvolvimento!", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> fazerLogout());

        return view;
    }

    private void fazerLogout() {
        mAuth.signOut();
        Toast.makeText(getContext(), "Você saiu! Até logo 👋", Toast.LENGTH_SHORT).show();

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new LoginFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}