/** UC15 - Etapa 9 - CRUD de Proprietários com Spring MVC/JDBC. */
$(document).ready(function () {
    ZeroAndarApp.inicializar();
    var idSelecionado = null;

    function render(lista) {
        var $lista = $('#listaProprietarios').empty();
        lista = (lista || []).filter(function (p) { return p.ativo !== false; });
        if (!lista.length) { $lista.append($('<div>').addClass('list-empty').text('Nenhum proprietário encontrado.')); return; }
        lista.forEach(function (p) {
            var id = p.idPessoaProprietario;
            var item = $('<div>').addClass('list-item')
                .toggleClass('selected', id === idSelecionado)
                .on('click', function () { selecionar(id); });
            item.append($('<div>').addClass('item-main')
                .append($('<div>').addClass('item-code').text('PROP-' + String(id).padStart(3, '0')))
                .append($('<div>').addClass('item-title').text(p.nome || ''))
                .append($('<div>').addClass('item-subtitle').text((p.email || '') + ' • ' + (p.telefone || ''))))
                .append($('<span>').addClass('item-badge badge-success').text('Ativo'));
            $lista.append(item);
        });
    }

    function carregar() {
        var busca = $.trim($('#buscaProprietario').val());
        return ZeroAndarApi.get('/proprietarios' + (busca ? '?busca=' + encodeURIComponent(busca) : ''))
            .then(function (lista) { render(lista); return lista; });
    }

    // Limpa somente o formulário, sem restaurar dados estáticos da Etapa 8.
    function limparFormulario() {
        idSelecionado = null;
        $('#proprietarioId').val('');
        $('#proprietarioCodigo').val('');
        $('#proprietarioNome').val('');
        $('#proprietarioCpf').val('');
        $('#proprietarioRg').val('');
        $('#proprietarioEmail').val('');
        $('#proprietarioTelefone').val('');
        $('#proprietarioEndereco').val('');
        $('#proprietarioCidade').val('');
        $('#proprietarioEstado').val('');
        $('#proprietarioDataCadastro').val('');
        $('#proprietarioAceitaContato').prop('checked', false);
        $('#proprietarioObservacoes').val('');
        $('#formProprietarioTitulo').text('Novo Proprietário');
        $('#listaProprietarios .list-item').removeClass('selected');
    }

    function selecionar(id) {
        ZeroAndarApi.get('/proprietarios/' + id).then(function (p) {
            idSelecionado = id;
            $('#formProprietarioTitulo').text('Dados do Proprietário');
            $('#proprietarioId').val(id);
            $('#proprietarioCodigo').val('PROP-' + String(id).padStart(3, '0'));
            $('#proprietarioNome').val(p.nome || '');
            $('#proprietarioCpf').val(p.cpf || '');
            $('#proprietarioRg').val(p.rg || '');
            $('#proprietarioEmail').val(p.email || '');
            $('#proprietarioTelefone').val(p.telefone || '');
            var e = p.endereco || {};
            $('#proprietarioEndereco').val((e.rua || '') + (e.numero ? ', ' + e.numero : ''));
            $('#proprietarioCidade').val(e.cidade || '');
            $('#proprietarioEstado').val(e.estado || '');
            $('#proprietarioDataCadastro').val(p.dataCadastro ? String(p.dataCadastro).substring(0, 10) : '');
            $('#proprietarioAceitaContato').prop('checked', !!p.aceitaContato);
            $('#proprietarioObservacoes').val(p.observacoes || '');
            return carregar();
        }).catch(function (e) { alert(e.message || 'Não foi possível carregar o proprietário.'); });
    }

    function dados() {
        var end = $('#proprietarioEndereco').val().split(',');
        return {
            nome: $.trim($('#proprietarioNome').val()),
            cpf: $.trim($('#proprietarioCpf').val()),
            rg: $.trim($('#proprietarioRg').val()),
            email: $.trim($('#proprietarioEmail').val()),
            telefone: $.trim($('#proprietarioTelefone').val()),
            rua: $.trim(end[0] || ''),
            numero: $.trim(end.slice(1).join(',')),
            cidade: $.trim($('#proprietarioCidade').val()),
            bairro: '',
            estado: $.trim($('#proprietarioEstado').val()).toUpperCase(),
            cep: '',
            dataCadastro: $('#proprietarioDataCadastro').val() || null,
            aceitaContato: $('#proprietarioAceitaContato').is(':checked'),
            observacoes: $('#proprietarioObservacoes').val(),
            ativo: true
        };
    }

    $('#formProprietario').on('submit', function (e) {
        e.preventDefault();
        var d = dados();
        var novo = !idSelecionado;
        if (!d.nome || !d.cpf || !d.email) { alert('Preencha nome, CPF e e-mail.'); return; }
        var url = novo ? '/proprietarios' : '/proprietarios/' + idSelecionado;
        var requisicao = novo ? ZeroAndarApi.post(url, d) : ZeroAndarApi.put(url, d);
        requisicao.then(function (p) {
            alert(novo ? 'Proprietário cadastrado com sucesso.' : 'Proprietário alterado com sucesso.');
            return carregar().then(function () {
                if (p && p.idPessoaProprietario) selecionar(p.idPessoaProprietario);
                else if (idSelecionado) selecionar(idSelecionado);
            });
        }).catch(function (e) { alert(e.message || 'Não foi possível salvar o proprietário.'); });
    });

    $('#btnNovoProprietario').on('click', function (e) { e.preventDefault(); limparFormulario(); });
    $('#btnLimparProprietario').on('click', function (e) { e.preventDefault(); limparFormulario(); });

    $('#btnExcluirProprietario').on('click', function (e) {
        e.preventDefault();
        if (!idSelecionado) { alert('Selecione um proprietário para excluir.'); return; }
        var idExcluir = idSelecionado;
        if (!confirm('Deseja realmente excluir este proprietário?')) return;
        ZeroAndarApi.remove('/proprietarios/' + idExcluir)
            .then(function () { limparFormulario(); return carregar(); })
            .then(function () { alert('Proprietário excluído com sucesso.'); })
            .catch(function (e) { alert(e.message || 'Não foi possível excluir o proprietário.'); });
    });

    $('#buscaProprietario').on('input', carregar);

    carregar().then(function (lista) {
        var ativos = (lista || []).filter(function (p) { return p.ativo !== false; });
        if (ativos.length) selecionar(ativos[0].idPessoaProprietario);
        else limparFormulario();
    });
});
