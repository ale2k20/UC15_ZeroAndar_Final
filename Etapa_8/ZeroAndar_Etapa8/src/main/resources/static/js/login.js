/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 *
 * Validação da tela de login com JavaScript/jQuery.
 * Nesta etapa nao ha back-end nem banco de dados: a autenticação e
 * simulada com uma pequena lista de usuários em memória (baseada nos
 * registros de exemplo do script database/zeroandar_db.sql). O
 * checkbox "Lembrar-me" salva apenas o e-mail no localStorage, sem
 * usar banco, conforme sugerido no LEIA-ME do projeto.
 */

$(document).ready(function () {

    var usuariosMock = [
        { email: 'alex@zeroandar.com', senha: 'senha123', nome: 'Alex Admin', papel: 'Administrador' },
        { email: 'ricardo@zeroandar.com', senha: 'senha123', nome: 'Ricardo Gomes', papel: 'Corretor' },
        { email: 'patricia@zeroandar.com', senha: 'senha123', nome: 'Patricia Lima', papel: 'Corretor' }
    ];

    var $form = $('#loginForm');
    var $email = $('#email');
    var $senha = $('#senha');
    var $lembrar = $('#lembrarMe');
    var $erro = $('#loginErro');

    // Se o usuario marcou "lembrar-me" numa visita anterior, preenche o e-mail.
    var emailSalvo = localStorage.getItem('zeroandar_email_lembrado');
    if (emailSalvo) {
        $email.val(emailSalvo);
        $lembrar.prop('checked', true);
    }

    function validarEmail(valor) {
        var regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(valor);
    }

    function marcarInvalido($campo, invalido) {
        $campo.toggleClass('is-invalid', invalido);
    }

    $form.on('submit', function (event) {
        event.preventDefault();
        $erro.text('');

        var email = $.trim($email.val());
        var senha = $senha.val();
        var valido = true;

        if (!validarEmail(email)) {
            marcarInvalido($email, true);
            valido = false;
        } else {
            marcarInvalido($email, false);
        }

        if (!senha || senha.length < 6) {
            marcarInvalido($senha, true);
            valido = false;
        } else {
            marcarInvalido($senha, false);
        }

        if (!valido) {
            $erro.text('Preencha um e-mail válido e uma senha com pelo menos 6 caracteres.');
            return;
        }

        var usuario = usuariosMock.filter(function (u) {
            return u.email.toLowerCase() === email.toLowerCase() && u.senha === senha;
        })[0];

        if (!usuario) {
            $erro.text('E-mail ou senha inválidos.');
            marcarInvalido($email, true);
            marcarInvalido($senha, true);
            return;
        }

        if ($lembrar.is(':checked')) {
            localStorage.setItem('zeroandar_email_lembrado', email);
        } else {
            localStorage.removeItem('zeroandar_email_lembrado');
        }

        // Guarda o usuario logado apenas para exibir no cabecalho (sem banco/sessao real).
        sessionStorage.setItem('zeroandar_usuario_nome', usuario.nome);
        sessionStorage.setItem('zeroandar_usuario_papel', usuario.papel);

        window.location.href = 'dashboard.html';
    });

    $('#linkEsqueceuSenha').on('click', function (event) {
        event.preventDefault();
        alert('Funcionalidade de recuperação de senha em construção. Ela será disponibilizada em uma próxima etapa do projeto.');
    });
});
