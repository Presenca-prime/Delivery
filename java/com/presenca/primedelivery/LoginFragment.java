package com.presenca.primedelivery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText editEmail, editSenha;
    private RadioGroup radioGroup;
    private RadioButton radioCliente, radioEntregador;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        mAuth = FirebaseAuth.getInstance();

        editEmail = view.findViewById(R.id.editEmailLogin);
        editSenha = view.findViewById(R.id.editSenhaLogin);
        Button btnEntrar = view.findViewById(R.id.btnEntrar);
        TextView textCadastrar = view.findViewById(R.id.textCadastrar);
        radioGroup = view.findViewById(R.id.radioGroupTipo);
        radioCliente = view.findViewById(R.id.radioCliente);
        radioEntregador = view.findViewById(R.id.radioEntregador);

        btnEntrar.setOnClickListener(v -> fazerLogin());

        textCadastrar.setOnClickListener(v -> irParaCadastro());

        return view;
    }

    private void fazerLogin() {
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(getContext(), "Preencha email e senha!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                buscarDadosUsuario();
            } else {
                Toast.makeText(getContext(), "Erro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarDadosUsuario() {
        String userId = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                if (usuario != null) {
                    usuario.setId(userId);
                    irParaDashboard(usuario);
                }
            } else {
                Toast.makeText(getContext(), "Dados do usuário não encontrados!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Erro ao buscar dados: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void irParaDashboard(Usuario usuario) {
        DashboardFragment dashboardFragment = new DashboardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nome", usuario.getNome());
        bundle.putString("tipo", usuario.getTipo());
        dashboardFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, dashboardFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void irParaCadastro() {
        CadastroFragment cadastroFragment = new CadastroFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, cadastroFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}