/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 *
 * Tela de Gestão de Corretores: lista + formulário (master-detail).
 * Dados mantidos em ZeroAndarMock.corretores (js/mock-data.js), em memória.
 */

$(document).ready(function () {

    var idSelecionado = null;
    var regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    var regexCpf = /^\d{3}\.\d{3}\.\d{3}-\d{2}$/;

    function renderizarLista(filtro) {
        var $lista = $('#listaCorretores').empty();
        var termo = (filtro || '').toLowerCase();

        var itens = ZeroAndarMock.corretores.filter(function (c) {
            if (!termo) return true;
            var alvo = (c.nome + ' ' + c.creci + ' ' + c.email).toLowerCase();
            return alvo.indexOf(termo) !== -1;
        });

        if (itens.length === 0) {
            $lista.append($('<div>').addClass('list-empty').text('Nenhum corretor encontrado.'));
            return;
        }

        itens.forEach(function (c) {
            var $item = $('<div>').addClass('list-item').attr('data-id', c.id);
            if (c.id === idSelecionado) $item.addClass('selected');

            var $main = $('<div>').addClass('item-main');
            $main.append($('<div>').addClass('item-code').text(c.creci));
            $main.append($('<div>').addClass('item-title').text(c.nome));
            $main.append($('<div>').addClass('item-subtitle').text(c.especialidade + ' • ' + c.email));
            $item.append($main);

            var badge = c.dataDesligamento
                ? $('<span>').addClass('item-badge badge-warning').text('Desligado')
                : $('<span>').addClass('item-badge badge-success').text('Ativo');
            $item.append(badge);

            $item.on('click', function () { selecionarCorretor(c.id); });
            $lista.append($item);
        });
    }

    function limparFormulario() {
        idSelecionado = null;
        $('#formCorretor')[0].reset();
        $('#corretorId').val('');
        $('#corretorCidade').val('Passo Fundo');
        $('#corretorEstado').val('RS');
        $('#corretorComissao').val('6.00');
        $('#formCorretorTitulo').text('Novo Corretor');
        $('.is-invalid').removeClass('is-invalid');
        renderizarLista($('#buscaCorretor').val());
    }

    function selecionarCorretor(id) {
        var c = ZeroAndarMock.corretores.filter(function (x) { return x.id === id; })[0];
        if (!c) return;

        idSelecionado = id;
        $('#formCorretorTitulo').text('Dados do Corretor');
        $('#corretorId').val(c.id);
        $('#corretorCreci').val(c.creci);
        $('#corretorNome').val(c.nome);
        $('#corretorCpf').val(c.cpf);
        $('#corretorRg').val(c.rg);
        $('#corretorEmail').val(c.email);
        $('#corretorTelefone').val(c.telefone);
        $('#corretorDataAdmissao').val(c.dataAdmissao);
        $('#corretorDataDesligamento').val(c.dataDesligamento);
        $('#corretorEspecialidade').val(c.especialidade);
        $('#corretorComissao').val(c.comissao);
        $('#corretorMeta').val(c.metaMensal);
        $('#corretorEndereco').val(c.rua + (c.numero ? ', ' + c.numero : ''));
        $('#corretorCidade').val(c.cidade);
        $('#corretorEstado').val(c.estado);
        $('#corretorObservacoes').val(c.observacoes);

        $('.is-invalid').removeClass('is-invalid');
        renderizarLista($('#buscaCorretor').val());
    }

    function validarFormulario() {
        var valido = true;

        var creciVazio = !$.trim($('#corretorCreci').val());
        $('#corretorCreci').toggleClass('is-invalid', creciVazio);
        if (creciVazio) valido = false;

        var nomeVazio = !$.trim($('#corretorNome').val());
        $('#corretorNome').toggleClass('is-invalid', nomeVazio);
        if (nomeVazio) valido = false;

        var cpfOk = regexCpf.test($.trim($('#corretorCpf').val()));
        $('#corretorCpf').toggleClass('is-invalid', !cpfOk);
        if (!cpfOk) valido = false;

        var emailOk = regexEmail.test($.trim($('#corretorEmail').val()));
        $('#corretorEmail').toggleClass('is-invalid', !emailOk);
        if (!emailOk) valido = false;

        return valido;
    }

    $('#formCorretor').on('submit', function (event) {
        event.preventDefault();
        if (!validarFormulario()) {
            alert('Verifique os campos em destaque: CRECI, nome, CPF (000.000.000-00) e e-mail são obrigatórios.');
            return;
        }

        var enderecoPartes = $('#corretorEndereco').val().split(',');

        var dados = {
            creci: $.trim($('#corretorCreci').val()),
            nome: $.trim($('#corretorNome').val()),
            cpf: $('#corretorCpf').val(),
            rg: $('#corretorRg').val(),
            email: $.trim($('#corretorEmail').val()),
            telefone: $('#corretorTelefone').val(),
            dataAdmissao: $('#corretorDataAdmissao').val(),
            dataDesligamento: $('#corretorDataDesligamento').val(),
            especialidade: $('#corretorEspecialidade').val(),
            comissao: Number($('#corretorComissao').val()) || 0,
            metaMensal: $('#corretorMeta').val() ? Number($('#corretorMeta').val()) : null,
            rua: $.trim(enderecoPartes[0] || ''),
            numero: $.trim(enderecoPartes[1] || ''),
            cidade: $('#corretorCidade').val(),
            estado: $('#corretorEstado').val(),
            observacoes: $('#corretorObservacoes').val()
        };

        if (idSelecionado) {
            var indice = ZeroAndarMock.corretores.findIndex(function (c) { return c.id === idSelecionado; });
            dados.id = idSelecionado;
            ZeroAndarMock.corretores[indice] = dados;
        } else {
            dados.id = ZeroAndarMock.proximoId(ZeroAndarMock.corretores);
            ZeroAndarMock.corretores.push(dados);
        }

        idSelecionado = dados.id;
        renderizarLista($('#buscaCorretor').val());
        selecionarCorretor(dados.id);
        alert('Corretor salvo com sucesso (em memória, sem persistência em banco nesta etapa).');
    });

    $('#btnNovoCorretor').on('click', limparFormulario);
    $('#btnLimparCorretor').on('click', limparFormulario);

    $('#btnExcluirCorretor').on('click', function () {
        if (!idSelecionado) {
            alert('Selecione um corretor na lista para excluir.');
            return;
        }
        var vinculados = ZeroAndarMock.imoveis.filter(function (i) { return i.corretorId === idSelecionado; });
        if (vinculados.length > 0) {
            alert('Não é possível excluir: existem imóveis vinculados a este corretor.');
            return;
        }
        if (!confirm('Deseja realmente excluir este corretor?')) return;

        ZeroAndarMock.corretores = ZeroAndarMock.corretores.filter(function (c) { return c.id !== idSelecionado; });
        limparFormulario();
    });

    $('#buscaCorretor').on('input', function () {
        renderizarLista($(this).val());
    });

    ZeroAndarApp.inicializar(function () {
        renderizarLista();
        if (ZeroAndarMock.corretores.length > 0) {
            selecionarCorretor(ZeroAndarMock.corretores[0].id);
        } else {
            limparFormulario();
        }
    });
});
