/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 *
 * Tela de Gestão de Proprietários: lista + formulário (master-detail).
 * Dados mantidos em ZeroAndarMock.proprietarios (js/mock-data.js), em memória.
 */

$(document).ready(function () {

    var idSelecionado = null;
    var regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    var regexCpf = /^\d{3}\.\d{3}\.\d{3}-\d{2}$/;

    function imoveisDoProprietario(proprietarioId) {
        return ZeroAndarMock.imoveis
            .filter(function (i) { return i.proprietarioId === proprietarioId; })
            .map(function (i) { return i.codigo; })
            .join(', ') || '(nenhum imóvel vinculado)';
    }

    function renderizarLista(filtro) {
        var $lista = $('#listaProprietarios').empty();
        var termo = (filtro || '').toLowerCase();

        var itens = ZeroAndarMock.proprietarios.filter(function (p) {
            if (!termo) return true;
            var alvo = (p.nome + ' ' + p.cpf + ' ' + p.email).toLowerCase();
            return alvo.indexOf(termo) !== -1;
        });

        if (itens.length === 0) {
            $lista.append($('<div>').addClass('list-empty').text('Nenhum proprietário encontrado.'));
            return;
        }

        itens.forEach(function (p) {
            var $item = $('<div>').addClass('list-item').attr('data-id', p.id);
            if (p.id === idSelecionado) $item.addClass('selected');

            var $main = $('<div>').addClass('item-main');
            $main.append($('<div>').addClass('item-code').text(p.codigo));
            $main.append($('<div>').addClass('item-title').text(p.nome));
            $main.append($('<div>').addClass('item-subtitle').text(p.email + ' • ' + p.telefone));
            $item.append($main);

            var badge = p.aceitaContato
                ? $('<span>').addClass('item-badge badge-success').text('Aceita contato')
                : $('<span>').addClass('item-badge badge-info').text('Sem contato');
            $item.append(badge);

            $item.on('click', function () { selecionarProprietario(p.id); });
            $lista.append($item);
        });
    }

    function limparFormulario() {
        idSelecionado = null;
        $('#formProprietario')[0].reset();
        $('#proprietarioId').val('');
        $('#proprietarioCodigo').val('(gerado ao salvar)');
        $('#proprietarioCidade').val('Passo Fundo');
        $('#proprietarioEstado').val('RS');
        $('#proprietarioAceitaContato').prop('checked', true);
        $('#proprietarioImoveis').val('(nenhum imóvel vinculado)');
        $('#formProprietarioTitulo').text('Novo Proprietário');
        $('.is-invalid').removeClass('is-invalid');
        renderizarLista($('#buscaProprietario').val());
    }

    function selecionarProprietario(id) {
        var p = ZeroAndarMock.proprietarios.filter(function (x) { return x.id === id; })[0];
        if (!p) return;

        idSelecionado = id;
        $('#formProprietarioTitulo').text('Dados do Proprietário');
        $('#proprietarioId').val(p.id);
        $('#proprietarioCodigo').val(p.codigo);
        $('#proprietarioNome').val(p.nome);
        $('#proprietarioCpf').val(p.cpf);
        $('#proprietarioRg').val(p.rg);
        $('#proprietarioEmail').val(p.email);
        $('#proprietarioTelefone').val(p.telefone);
        $('#proprietarioEndereco').val(p.rua + (p.numero ? ', ' + p.numero : ''));
        $('#proprietarioCidade').val(p.cidade);
        $('#proprietarioEstado').val(p.estado);
        $('#proprietarioDataCadastro').val(p.dataCadastro);
        $('#proprietarioAceitaContato').prop('checked', p.aceitaContato);
        $('#proprietarioImoveis').val(imoveisDoProprietario(p.id));
        $('#proprietarioObservacoes').val(p.observacoes);

        $('.is-invalid').removeClass('is-invalid');
        renderizarLista($('#buscaProprietario').val());
    }

    function validarFormulario() {
        var valido = true;

        var nomeVazio = !$.trim($('#proprietarioNome').val());
        $('#proprietarioNome').toggleClass('is-invalid', nomeVazio);
        if (nomeVazio) valido = false;

        var cpfOk = regexCpf.test($.trim($('#proprietarioCpf').val()));
        $('#proprietarioCpf').toggleClass('is-invalid', !cpfOk);
        if (!cpfOk) valido = false;

        var emailOk = regexEmail.test($.trim($('#proprietarioEmail').val()));
        $('#proprietarioEmail').toggleClass('is-invalid', !emailOk);
        if (!emailOk) valido = false;

        return valido;
    }

    $('#formProprietario').on('submit', function (event) {
        event.preventDefault();
        if (!validarFormulario()) {
            alert('Verifique os campos em destaque: nome, CPF (000.000.000-00) e e-mail são obrigatórios.');
            return;
        }

        var enderecoPartes = $('#proprietarioEndereco').val().split(',');

        var dados = {
            nome: $.trim($('#proprietarioNome').val()),
            cpf: $('#proprietarioCpf').val(),
            rg: $('#proprietarioRg').val(),
            email: $.trim($('#proprietarioEmail').val()),
            telefone: $('#proprietarioTelefone').val(),
            rua: $.trim(enderecoPartes[0] || ''),
            numero: $.trim(enderecoPartes[1] || ''),
            cidade: $('#proprietarioCidade').val(),
            estado: $('#proprietarioEstado').val(),
            dataCadastro: $('#proprietarioDataCadastro').val(),
            aceitaContato: $('#proprietarioAceitaContato').is(':checked'),
            observacoes: $('#proprietarioObservacoes').val()
        };

        if (idSelecionado) {
            var indice = ZeroAndarMock.proprietarios.findIndex(function (p) { return p.id === idSelecionado; });
            dados.id = idSelecionado;
            dados.codigo = ZeroAndarMock.proprietarios[indice].codigo;
            dados.imoveisVinculados = ZeroAndarMock.proprietarios[indice].imoveisVinculados;
            ZeroAndarMock.proprietarios[indice] = dados;
        } else {
            dados.id = ZeroAndarMock.proximoId(ZeroAndarMock.proprietarios);
            dados.codigo = 'PROP-' + String(dados.id).padStart(3, '0');
            dados.imoveisVinculados = '';
            ZeroAndarMock.proprietarios.push(dados);
        }

        idSelecionado = dados.id;
        renderizarLista($('#buscaProprietario').val());
        selecionarProprietario(dados.id);
        alert('Proprietário salvo com sucesso (em memória, sem persistência em banco nesta etapa).');
    });

    $('#btnNovoProprietario').on('click', limparFormulario);
    $('#btnLimparProprietario').on('click', limparFormulario);

    $('#btnExcluirProprietario').on('click', function () {
        if (!idSelecionado) {
            alert('Selecione um proprietário na lista para excluir.');
            return;
        }
        var vinculados = ZeroAndarMock.imoveis.filter(function (i) { return i.proprietarioId === idSelecionado; });
        if (vinculados.length > 0) {
            alert('Não é possível excluir: existem imóveis vinculados a este proprietário.');
            return;
        }
        if (!confirm('Deseja realmente excluir este proprietário?')) return;

        ZeroAndarMock.proprietarios = ZeroAndarMock.proprietarios.filter(function (p) { return p.id !== idSelecionado; });
        limparFormulario();
    });

    $('#buscaProprietario').on('input', function () {
        renderizarLista($(this).val());
    });

    ZeroAndarApp.inicializar(function () {
        renderizarLista();
        if (ZeroAndarMock.proprietarios.length > 0) {
            selecionarProprietario(ZeroAndarMock.proprietarios[0].id);
        } else {
            limparFormulario();
        }
    });
});
