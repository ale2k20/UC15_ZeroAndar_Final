/** UC15 - Etapa 9 - Login integrado ao Spring MVC e MySQL. */
$(document).ready(function () {
    var $form = $('#loginForm'), $email = $('#email'), $senha = $('#senha'), $lembrar = $('#lembrarMe'), $erro = $('#loginErro');
    var salvo = localStorage.getItem('zeroandar_email_lembrado');
    if (salvo) { $email.val(salvo); $lembrar.prop('checked', true); }

    $form.on('submit', function (event) {
        event.preventDefault(); $erro.text('');
        var email = $.trim($email.val()), senha = $senha.val();
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email) || senha.length < 6) {
            $erro.text('Preencha um e-mail válido e uma senha com pelo menos 6 caracteres.'); return;
        }
        ZeroAndarApi.post('/login', {email:email, senha:senha}).then(function (usuario) {
            if ($lembrar.is(':checked')) localStorage.setItem('zeroandar_email_lembrado', email);
            else localStorage.removeItem('zeroandar_email_lembrado');
            sessionStorage.setItem('zeroandar_usuario_nome', usuario.nome);
            sessionStorage.setItem('zeroandar_usuario_papel', usuario.email === 'admin@zandar.com' ? 'Administrador' : 'Corretor');
            window.location.href = 'dashboard.html';
        }).catch(function (e) { $erro.text(e.message); });
    });

    $('#linkEsqueceuSenha').on('click', function (event) { event.preventDefault(); alert('Funcionalidade em construção.'); });
});
