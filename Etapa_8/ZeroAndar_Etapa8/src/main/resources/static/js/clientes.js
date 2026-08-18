/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 *
 * Tela de Gestão de Clientes: lista + formulário (master-detail).
 * Dados mantidos em ZeroAndarMock.clientes (js/mock-data.js), em memória.
 */

$(document).ready(function () {

    var idSelecionado = null;
    var regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    var regexCpf = /^\d{3}\.\d{3}\.\d{3}-\d{2}$/;

    function popularSelects() {
        var $cor = $('#clienteCorretor').empty();
        $cor.append($('<option>').val('').text('Selecione o corretor'));
        ZeroAndarMock.corretores.forEach(function (c) {
            $cor.append($('<option>').val(c.id).text(c.nome + ' (' + c.creci + ')'));
        });
    }

    function renderizarLista(filtro) {
        var $lista = $('#listaClientes').empty();
        var termo = (filtro || '').toLowerCase();

        var itens = ZeroAndarMock.clientes.filter(function (cliente) {
            if (!termo) return true;
            var alvo = (cliente.nome + ' ' + cliente.cpf + ' ' + cliente.email).toLowerCase();
            return alvo.indexOf(termo) !== -1;
        });

        if (itens.length === 0) {
            $lista.append($('<div>').addClass('list-empty').text('Nenhum cliente encontrado.'));
            return;
        }

        itens.forEach(function (cliente) {
            var $item = $('<div>').addClass('list-item').attr('data-id', cliente.id);
            if (cliente.id === idSelecionado) $item.addClass('selected');

            var $main = $('<div>').addClass('item-main');
            $main.append($('<div>').addClass('item-code').text(cliente.codigo));
            $main.append($('<div>').addClass('item-title').text(cliente.nome));
            $main.append($('<div>').addClass('item-subtitle').text(cliente.email + ' • ' + cliente.telefone));
            $item.append($main);

            var badge = cliente.ativo
                ? $('<span>').addClass('item-badge badge-success').text('Ativo')
                : $('<span>').addClass('item-badge badge-warning').text('Inativo');
            $item.append(badge);

            $item.on('click', function () { selecionarCliente(cliente.id); });
            $lista.append($item);
        });
    }

    function limparFormulario() {
        idSelecionado = null;
        $('#formCliente')[0].reset();
        $('#clienteId').val('');
        $('#clienteCodigo').val('(gerado ao salvar)');
        $('#clienteCidade').val('Passo Fundo');
        $('#clienteEstado').val('RS');
        $('#clienteAtivo').prop('checked', true);
        $('#formClienteTitulo').text('Novo Cliente');
        $('.is-invalid').removeClass('is-invalid');
        renderizarLista($('#buscaCliente').val());
    }

    function selecionarCliente(id) {
        var cliente = ZeroAndarMock.clientes.filter(function (c) { return c.id === id; })[0];
        if (!cliente) return;

        idSelecionado = id;
        $('#formClienteTitulo').text('Dados do Cliente');
        $('#clienteId').val(cliente.id);
        $('#clienteCodigo').val(cliente.codigo);
        $('#clienteNome').val(cliente.nome);
        $('#clienteCpf').val(cliente.cpf);
        $('#clienteRg').val(cliente.rg);
        $('#clienteEmail').val(cliente.email);
        $('#clienteTelefone').val(cliente.telefone);
        $('#clienteDataNascimento').val(cliente.dataNascimento);
        $('#clienteEndereco').val(cliente.endereco);
        $('#clienteCidade').val(cliente.cidade);
        $('#clienteEstado').val(cliente.estado);
        $('#clienteCorretor').val(cliente.corretorId || '');
        $('#clienteInteresse').val(cliente.tipoInteresse);
        $('#clienteAtivo').prop('checked', cliente.ativo);
        $('#clienteObservacoes').val(cliente.observacoes);

        $('.is-invalid').removeClass('is-invalid');
        renderizarLista($('#buscaCliente').val());
    }

    function validarFormulario() {
        var valido = true;

        var nomeVazio = !$.trim($('#clienteNome').val());
        $('#clienteNome').toggleClass('is-invalid', nomeVazio);
        if (nomeVazio) valido = false;

        var cpfOk = regexCpf.test($.trim($('#clienteCpf').val()));
        $('#clienteCpf').toggleClass('is-invalid', !cpfOk);
        if (!cpfOk) valido = false;

        var emailOk = regexEmail.test($.trim($('#clienteEmail').val()));
        $('#clienteEmail').toggleClass('is-invalid', !emailOk);
        if (!emailOk) valido = false;

        return valido;
    }

    $('#formCliente').on('submit', function (event) {
        event.preventDefault();
        if (!validarFormulario()) {
            alert('Verifique os campos em destaque: nome, CPF (000.000.000-00) e e-mail são obrigatórios.');
            return;
        }

        var dados = {
            nome: $.trim($('#clienteNome').val()),
            cpf: $('#clienteCpf').val(),
            rg: $('#clienteRg').val(),
            email: $.trim($('#clienteEmail').val()),
            telefone: $('#clienteTelefone').val(),
            dataNascimento: $('#clienteDataNascimento').val(),
            endereco: $('#clienteEndereco').val(),
            cidade: $('#clienteCidade').val(),
            estado: $('#clienteEstado').val(),
            corretorId: $('#clienteCorretor').val() ? Number($('#clienteCorretor').val()) : null,
            tipoInteresse: $('#clienteInteresse').val(),
            ativo: $('#clienteAtivo').is(':checked'),
            observacoes: $('#clienteObservacoes').val()
        };

        if (idSelecionado) {
            var indice = ZeroAndarMock.clientes.findIndex(function (c) { return c.id === idSelecionado; });
            dados.id = idSelecionado;
            dados.codigo = ZeroAndarMock.clientes[indice].codigo;
            ZeroAndarMock.clientes[indice] = dados;
        } else {
            dados.id = ZeroAndarMock.proximoId(ZeroAndarMock.clientes);
            dados.codigo = 'CLI-' + String(dados.id).padStart(3, '0');
            ZeroAndarMock.clientes.push(dados);
        }

        idSelecionado = dados.id;
        renderizarLista($('#buscaCliente').val());
        selecionarCliente(dados.id);
        alert('Cliente salvo com sucesso (em memória, sem persistência em banco nesta etapa).');
    });

    $('#btnNovoCliente').on('click', limparFormulario);
    $('#btnLimparCliente').on('click', limparFormulario);

    $('#btnExcluirCliente').on('click', function () {
        if (!idSelecionado) {
            alert('Selecione um cliente na lista para excluir.');
            return;
        }
        if (!confirm('Deseja realmente excluir este cliente?')) return;

        ZeroAndarMock.clientes = ZeroAndarMock.clientes.filter(function (c) { return c.id !== idSelecionado; });
        limparFormulario();
    });

    $('#buscaCliente').on('input', function () {
        renderizarLista($(this).val());
    });

    ZeroAndarApp.inicializar(function () {
        popularSelects();
        renderizarLista();
        if (ZeroAndarMock.clientes.length > 0) {
            selecionarCliente(ZeroAndarMock.clientes[0].id);
        } else {
            limparFormulario();
        }
    });
});
