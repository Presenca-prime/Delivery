package com.presenca.primedelivery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class CadastroFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText editNome, editEmail, editSenha, editVeiculo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro, container, false);

        mAuth = FirebaseAuth.getInstance();

        editNome = view.findViewById(R.id.editNomeCadastro);
        editEmail = view.findViewById(R.id.editEmailCadastro);
        editSenha = view.findViewById(R.id.editSenhaCadastro);
        editVeiculo = view.findViewById(R.id.editVeiculoCadastro);
        Button btnCadastrar = view.findViewById(R.id.btnCadastrar);
        TextView textVoltarLogin = view.findViewById(R.id.textVoltarLogin);

        btnCadastrar.setOnClickListener(v -> fazerCadastro());
        textVoltarLogin.setOnClickListener(v -> voltarParaLogin());

        return view;
    }

    private void fazerCadastro() {
        String nome = editNome.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();
        String veiculo = editVeiculo.getText().toString().trim();
        String tipo = veiculo.isEmpty() ? "cliente" : "entregador";

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(getContext(), "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                salvarDadosNoFirestore(nome, email, tipo, veiculo);
            } else {
                Toast.makeText(getContext(), "Erro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salvarDadosNoFirestore(String nome, String email, String tipo, String veiculo) {
        String userId = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuario = new HashMap<>();
        usuario.put("nome", nome);
        usuario.put("email", email);
        usuario.put("tipo", tipo);
        usuario.put("veiculo", veiculo);

        db.collection("usuarios").document(userId).set(usuario).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), "✅ Cadastro realizado! Faça login.", Toast.LENGTH_SHORT).show();
            voltarParaLogin();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void voltarParaLogin() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new LoginFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}