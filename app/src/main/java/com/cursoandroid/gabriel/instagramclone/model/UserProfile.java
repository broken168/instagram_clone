package com.cursoandroid.gabriel.instagramclone.model;

public class UserProfile {

    public UserProfile() {
    }

    private Long idUser;

    private String email;

    private String profileImage_path_name;

    private String username;

    private Long seguidores;

    private Long seguindo;

    private Long postagens;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getProfileImage_path_name() {
        return profileImage_path_name;
    }

    public void setProfileImage_path_name(String profileImage_path_name) {
        this.profileImage_path_name = profileImage_path_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(Long seguidores) {
        this.seguidores = seguidores;
    }

    public Long getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(Long seguindo) {
        this.seguindo = seguindo;
    }

    public Long getPostagens() {
        return postagens;
    }

    public void setPostagens(Long postagens) {
        this.postagens = postagens;
    }
}
