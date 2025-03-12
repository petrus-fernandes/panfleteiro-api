package br.com.promo.panfleteiro.response;

import br.com.promo.panfleteiro.entity.UserRole;

public record UserRequest(String login, String password, UserRole role) {

}
