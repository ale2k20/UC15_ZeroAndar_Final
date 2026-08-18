/** UC15 - Etapa 9 - CRUD de Corretores com Spring MVC/JDBC. */
$(document).ready(function () {
    ZeroAndarApp.inicializar();
    var idSelecionado = null;

    function render(lista) {
        var $lista = $('#listaCorretores').empty();
        lista = (lista || []).filter(function (c) { return c.ativo !== false; });
        if (!lista.length) { $lista.append($('<div>').addClass('list-empty').text('Nenhum corretor encontrado.')); return; }
        lista.forEach(function (c) {
            var id = c.idPessoaCorretor;
            var item = $('<div>').addClass('list-item')
                .toggleClass('selected', id === idSelecionado)
                .on('click', function () { selecionar(id); });
            item.append($('<div>').addClass('item-main')
                .append($('<div>').addClass('item-code').text(c.creci || ('COR-' + id)))
                .append($('<div>').addClass('item-title').text(c.nome || ''))
                .append($('<div>').addClass('item-subtitle').text((c.email || '') + ' • ' + (c.telefone || ''))))
                .append($('<span>').addClass('item-badge badge-success').text('Ativo'));
            $lista.append(item);
        });
    }

    function carregar() {
        var busca = $.trim($('#buscaCorretor').val());
        return ZeroAndarApi.get('/corretores' + (busca ? '?busca=' + encodeURIComponent(busca) : ''))
            .then(function (lista) { render(lista); return lista; });
    }

    // Limpa somente o formulário, sem restaurar os valores estáticos da Etapa 8.
    function limparFormulario() {
        idSelecionado = null;
        $('#corretorId').val('');
        $('#corretorCodigo').val('');
        $('#corretorNome').val('');
        $('#corretorCpf').val('');
        $('#corretorRg').val('');
        $('#corretorEmail').val('');
        $('#corretorTelefone').val('');
        $('#corretorCreci').val('');
        $('#corretorComissao').val('');
        $('#corretorDataAdmissao').val('');
        $('#corretorDataDesligamento').val('');
        $('#corretorEspecialidade').val('');
        $('#corretorMeta').val('');
        $('#corretorEndereco').val('');
        $('#corretorCidade').val('');
        $('#corretorEstado').val('');
        $('#corretorAtivo').prop('checked', true);
        $('#corretorObservacoes').val('');
        $('#formCorretorTitulo').text('Novo Corretor');
        $('#listaCorretores .list-item').removeClass('selected');
    }

    function selecionar(id) {
        ZeroAndarApi.get('/corretores/' + id).then(function (c) {
            idSelecionado = id;
            $('#formCorretorTitulo').text('Dados do Corretor');
            $('#corretorId').val(id);
            $('#corretorCodigo').val(c.creci || '');
            $('#corretorNome').val(c.nome || '');
            $('#corretorCpf').val(c.cpf || '');
            $('#corretorRg').val(c.rg || '');
            $('#corretorEmail').val(c.email || '');
            $('#corretorTelefone').val(c.telefone || '');
            $('#corretorCreci').val(c.creci || '');
            $('#corretorComissao').val(c.comissaoPercentual || 0);
            $('#corretorDataAdmissao').val(c.dataAdmissao ? String(c.dataAdmissao).substring(0, 10) : '');
            $('#corretorDataDesligamento').val(c.dataDesligamento ? String(c.dataDesligamento).substring(0, 10) : '');
            $('#corretorEspecialidade').val(c.especialidade || '');
            $('#corretorMeta').val(c.metaMensal || 0);
            var e = c.endereco || {};
            $('#corretorEndereco').val((e.rua || '') + (e.numero ? ', ' + e.numero : ''));
            $('#corretorCidade').val(e.cidade || '');
            $('#corretorEstado').val(e.estado || '');
            $('#corretorAtivo').prop('checked', !!c.ativo);
            $('#corretorObservacoes').val(c.observacoes || '');
            return carregar();
        }).catch(function (e) { alert(e.message || 'Não foi possível carregar o corretor.'); });
    }

    function dados() {
        var end = $('#corretorEndereco').val().split(',');
        return {
            nome: $.trim($('#corretorNome').val()),
            cpf: $.trim($('#corretorCpf').val()),
            rg: $.trim($('#corretorRg').val()),
            email: $.trim($('#corretorEmail').val()),
            telefone: $.trim($('#corretorTelefone').val()),
            creci: $.trim($('#corretorCreci').val()),
            comissaoPercentual: Number($('#corretorComissao').val()) || 0,
            dataAdmissao: $('#corretorDataAdmissao').val() || null,
            dataDesligamento: $('#corretorDataDesligamento').val() || null,
            especialidade: $('#corretorEspecialidade').val(),
            metaMensal: Number($('#corretorMeta').val()) || 0,
            rua: $.trim(end[0] || ''),
            numero: $.trim(end.slice(1).join(',')),
            cidade: $.trim($('#corretorCidade').val()),
            bairro: '',
            estado: $.trim($('#corretorEstado').val()).toUpperCase(),
            cep: '',
            ativo: $('#corretorAtivo').length ? $('#corretorAtivo').is(':checked') : true,
            observacoes: $('#corretorObservacoes').val()
        };
    }

    $('#formCorretor').on('submit', function (e) {
        e.preventDefault();
        var d = dados();
        var novo = !idSelecionado;
        if (!d.nome || !d.cpf || !d.email || !d.creci) { alert('Preencha nome, CPF, e-mail e CRECI.'); return; }
        var url = novo ? '/corretores' : '/corretores/' + idSelecionado;
        var requisicao = novo ? ZeroAndarApi.post(url, d) : ZeroAndarApi.put(url, d);
        requisicao.then(function (c) {
            alert(novo ? 'Corretor cadastrado com sucesso.' : 'Corretor alterado com sucesso.');
            return carregar().then(function () {
                if (c && c.idPessoaCorretor) selecionar(c.idPessoaCorretor);
                else if (idSelecionado) selecionar(idSelecionado);
            });
        }).catch(function (e) { alert(e.message || 'Não foi possível salvar o corretor.'); });
    });

    $('#btnNovoCorretor').on('click', function (e) { e.preventDefault(); limparFormulario(); });
    $('#btnLimparCorretor').on('click', function (e) { e.preventDefault(); limparFormulario(); });

    $('#btnExcluirCorretor').on('click', function (e) {
        e.preventDefault();
        if (!idSelecionado) { alert('Selecione um corretor para excluir.'); return; }
        var idExcluir = idSelecionado;
        if (!confirm('Deseja realmente excluir este corretor?')) return;
        // O endpoint de exclusão do corretor usa o CRECI como identificador de negócio.
        ZeroAndarApi.get('/corretores/' + idExcluir).then(function (c) {
            return ZeroAndarApi.remove('/corretores/' + idExcluir);
        }).then(function () {
            limparFormulario();
            return carregar();
        }).then(function () { alert('Corretor excluído com sucesso.'); })
          .catch(function (e) { alert(e.message || 'Não foi possível excluir o corretor.'); });
    });

    $('#buscaCorretor').on('input', carregar);

    carregar().then(function (lista) {
        var ativos = (lista || []).filter(function (c) { return c.ativo !== false; });
        if (ativos.length) selecionar(ativos[0].idPessoaCorretor);
        else limparFormulario();
    });
});
