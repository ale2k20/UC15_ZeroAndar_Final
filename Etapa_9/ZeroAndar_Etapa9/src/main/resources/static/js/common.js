/**
 * UC15 - Projeto Integrador 3 - Etapa 9
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.9
 *
 * Comportamento comum as telas internas do sistema:
 * - Marca o item de menu correspondente a pagina atual como ativo.
 * - Exibe aviso de "funcionalidade em construção" para os menus que
 *   ainda nao foram implementados (Agenda, Negociações, Pesquisa,
 *   Relatórios, Configurações).
 * - Controla o menu suspenso do usuário (avatar no cabeçalho) e o logout.
 *
 * Chamar ZeroAndarApp.inicializar() no $(document).ready() de cada pagina.
 */

var ZeroAndarApp = (function ($) {

    /** Modal (Bootstrap) usado para o aviso de funcionalidade em construção. */
    function garantirModalConstrucao() {
        if ($('#modalConstrucao').length > 0) {
            return;
        }
        var modalHtml =
            '<div class="modal fade" id="modalConstrucao" tabindex="-1" aria-hidden="true">' +
            '  <div class="modal-dialog modal-dialog-centered">' +
            '    <div class="modal-content">' +
            '      <div class="modal-header">' +
            '        <h5 class="modal-title">⚙️ Funcionalidade em construção</h5>' +
            '        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>' +
            '      </div>' +
            '      <div class="modal-body">' +
            '        <p id="modalConstrucaoTexto">Esta tela ainda está em desenvolvimento e será disponibilizada em uma próxima etapa do projeto.</p>' +
            '      </div>' +
            '      <div class="modal-footer">' +
            '        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Entendi</button>' +
            '      </div>' +
            '    </div>' +
            '  </div>' +
            '</div>';
        $('body').append(modalHtml);
    }

    /** Exibe o aviso de "em construção" para o nome de tela informado. */
    function avisarEmConstrucao(nomeTela) {
        garantirModalConstrucao();
        $('#modalConstrucaoTexto').text(
            'A tela "' + nomeTela + '" ainda está em construção e será implementada em uma próxima etapa do projeto.'
        );
        var modal = new bootstrap.Modal(document.getElementById('modalConstrucao'));
        modal.show();
    }

    /** Marca como ativo o item do menu lateral correspondente a pagina atual. */
    function marcarMenuAtivo() {
        var paginaAtual = $('body').data('page');
        $('.menu-item').removeClass('active');
        $('.menu-item[data-page="' + paginaAtual + '"]').addClass('active');
    }

    /** Liga o clique nos itens marcados como "em construção". */
    function ligarMenusEmConstrucao() {
        $('.menu-item[data-em-construcao="true"]').on('click', function (event) {
            event.preventDefault();
            var nomeTela = $(this).find('span').last().text();
            avisarEmConstrucao(nomeTela);
        });
    }

    /** Abre/fecha o menu suspenso do usuário no cabeçalho. */
    function ligarMenuUsuario() {
        $(document).on('click', '#userMenu', function (event) {
            event.stopPropagation();
            $(this).toggleClass('open');
        });
        $(document).on('click', function () {
            $('#userMenu').removeClass('open');
        });
        $(document).on('click', '#btnSair', function (event) {
            event.preventDefault();
            ZeroAndarApi.post('/login/sair', {}).finally(function () { window.location.href = 'login.html'; });
        });
        $(document).on('click', '#btnTrocarUsuario', function (event) {
            event.preventDefault();
            avisarEmConstrucao('Troca de usuário');
        });
    }

    /** Preenche nome/cargo do usuário logado (guardados no sessionStorage no login). */
    function preencherUsuarioLogado() {
        var nome = sessionStorage.getItem('zeroandar_usuario_nome') || 'João Silva';
        var papel = sessionStorage.getItem('zeroandar_usuario_papel') || 'Usuário';
        $('#userNameLabel').text(nome);
        $('#userRoleLabel').text(papel);
    }

    /** Inicializa o comportamento comum de uma tela interna. */
    function inicializar(callback) {
        marcarMenuAtivo();
        ligarMenusEmConstrucao();
        ligarMenuUsuario();
        preencherUsuarioLogado();
        if (typeof callback === 'function') {
            callback();
        }
    }

    return {
        inicializar: inicializar,
        avisarEmConstrucao: avisarEmConstrucao
    };

})(jQuery);
