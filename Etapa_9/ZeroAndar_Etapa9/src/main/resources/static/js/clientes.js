/** UC15 - Etapa 9 - CRUD de Clientes com Spring MVC/JDBC. */
$(document).ready(function () {
    ZeroAndarApp.inicializar();
    var idSelecionado = null;

    function render(lista) {
        var $lista = $('#listaClientes').empty();
        lista = (lista || []).filter(function (c) { return c.ativo !== false; });
        if (!lista.length) { $lista.append($('<div>').addClass('list-empty').text('Nenhum cliente encontrado.')); return; }
        lista.forEach(function (c) {
            var id = c.idPessoaCliente;
            var item = $('<div>').addClass('list-item')
                .toggleClass('selected', id === idSelecionado)
                .on('click', function () { selecionar(id); });
            item.append($('<div>').addClass('item-main')
                .append($('<div>').addClass('item-code').text('CLI-' + String(id).padStart(3, '0')))
                .append($('<div>').addClass('item-title').text(c.nome || ''))
                .append($('<div>').addClass('item-subtitle').text((c.email || '') + ' • ' + (c.telefone || ''))))
                .append($('<span>').addClass('item-badge badge-success').text('Ativo'));
            $lista.append(item);
        });
    }

    function carregar() {
        var busca = $.trim($('#buscaCliente').val());
        return ZeroAndarApi.get('/clientes' + (busca ? '?busca=' + encodeURIComponent(busca) : ''))
            .then(function (lista) { render(lista); return lista; });
    }

    // Limpa somente o formulário. Não usa form.reset(), pois o HTML da Etapa 8
    // possui dados estáticos que voltariam para os campos.
    function limparFormulario() {
        idSelecionado = null;
        $('#clienteId').val('');
        $('#clienteCodigo').val('');
        $('#clienteNome').val('');
        $('#clienteCpf').val('');
        $('#clienteRg').val('');
        $('#clienteEmail').val('');
        $('#clienteTelefone').val('');
        $('#clienteDataNascimento').val('');
        $('#clienteEndereco').val('');
        $('#clienteCidade').val('');
        $('#clienteEstado').val('');
        $('#clienteCorretor').val('');
        $('#clienteInteresse').val('');
        $('#clienteAtivo').prop('checked', true);
        $('#clienteObservacoes').val('');
        $('#formClienteTitulo').text('Novo Cliente');
        $('#listaClientes .list-item').removeClass('selected');
    }

    function selecionar(id) {
        ZeroAndarApi.get('/clientes/' + id).then(function (c) {
            idSelecionado = id;
            $('#formClienteTitulo').text('Dados do Cliente');
            $('#clienteId').val(id);
            $('#clienteCodigo').val('CLI-' + String(id).padStart(3, '0'));
            $('#clienteNome').val(c.nome || '');
            $('#clienteCpf').val(c.cpf || '');
            $('#clienteRg').val(c.rg || '');
            $('#clienteEmail').val(c.email || '');
            $('#clienteTelefone').val(c.telefone || '');
            $('#clienteDataNascimento').val(c.dataNascimento ? String(c.dataNascimento).substring(0, 10) : '');
            var e = c.endereco || {};
            $('#clienteEndereco').val((e.rua || '') + (e.numero ? ', ' + e.numero : ''));
            $('#clienteCidade').val(e.cidade || '');
            $('#clienteEstado').val(e.estado || '');
            $('#clienteInteresse').val(c.origemCadastro || '');
            $('#clienteAtivo').prop('checked', !!c.ativo);
            $('#clienteObservacoes').val(c.observacoes || '');
            return carregar();
        }).catch(function (e) { alert(e.message || 'Não foi possível carregar o cliente.'); });
    }

    function dados() {
        var end = $('#clienteEndereco').val().split(',');
        return {
            nome: $.trim($('#clienteNome').val()),
            cpf: $.trim($('#clienteCpf').val()),
            rg: $.trim($('#clienteRg').val()),
            email: $.trim($('#clienteEmail').val()),
            telefone: $.trim($('#clienteTelefone').val()),
            dataNascimento: $('#clienteDataNascimento').val() || null,
            rua: $.trim(end[0] || ''),
            numero: $.trim(end.slice(1).join(',')),
            cidade: $.trim($('#clienteCidade').val()),
            bairro: '',
            estado: $.trim($('#clienteEstado').val()).toUpperCase(),
            cep: '',
            origemCadastro: $('#clienteInteresse').val(),
            ativo: $('#clienteAtivo').is(':checked'),
            observacoes: $('#clienteObservacoes').val()
        };
    }

    $('#formCliente').on('submit', function (e) {
        e.preventDefault();
        var d = dados();
        var novo = !idSelecionado;
        if (!d.nome || !d.cpf || !d.email) {
            alert('Preencha nome, CPF e e-mail.');
            return;
        }
        var url = novo ? '/clientes' : '/clientes/' + idSelecionado;
        var requisicao = novo ? ZeroAndarApi.post(url, d) : ZeroAndarApi.put(url, d);
        requisicao.then(function (c) {
            alert(novo ? 'Cliente cadastrado com sucesso.' : 'Cliente alterado com sucesso.');
            return carregar().then(function () {
                if (c && c.idPessoaCliente) selecionar(c.idPessoaCliente);
                else if (idSelecionado) selecionar(idSelecionado);
            });
        }).catch(function (e) { alert(e.message || 'Não foi possível salvar o cliente.'); });
    });

    $('#btnNovoCliente').on('click', function (e) { e.preventDefault(); limparFormulario(); });
    $('#btnLimparCliente').on('click', function (e) { e.preventDefault(); limparFormulario(); });

    $('#btnExcluirCliente').on('click', function (e) {
        e.preventDefault();
        if (!idSelecionado) { alert('Selecione um cliente para excluir.'); return; }
        var idExcluir = idSelecionado;
        if (!confirm('Deseja realmente excluir este cliente?')) return;
        ZeroAndarApi.remove('/clientes/' + idExcluir)
            .then(function () { limparFormulario(); return carregar(); })
            .then(function () { alert('Cliente excluído com sucesso.'); })
            .catch(function (e) { alert(e.message || 'Não foi possível excluir o cliente.'); });
    });

    $('#buscaCliente').on('input', carregar);

    carregar().then(function (lista) {
        var ativos = (lista || []).filter(function (c) { return c.ativo !== false; });
        if (ativos.length) selecionar(ativos[0].idPessoaCliente);
        else limparFormulario();
    });
});
