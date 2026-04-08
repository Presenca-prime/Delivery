package com.example.backend.dto.response;
import com.example.backend.entity.administrador;
import lombok.Data;

@Data
public class AdministradorResponse {
    private Integer id;
    private UsuarioResponse usuario;
    private String cargo;
    private administrador.NivelAcesso nivelAcesso;
    
    public static AdministradorResponse fromEntity(administrador.Administrador admin) {
        AdministradorResponse response = new AdministradorResponse();
        response.setId(admin.getId());
        response.setCargo(admin.getCargo());
        response.setNivelAcesso(admin.getNivelAcesso());
        if (admin.getUsuario() != null) {
            response.setUsuario(UsuarioResponse.fromEntity(admin.getUsuario()));
        }
        return response;
    }
}