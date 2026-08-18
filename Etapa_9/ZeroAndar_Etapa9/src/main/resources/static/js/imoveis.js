/** UC15 - Etapa 9 - CRUD de Imóveis com Spring MVC/JDBC. */
$(document).ready(function () {
    ZeroAndarApp.inicializar();
    var idSelecionado = null;

    function render(lista) {
        var $lista = $('#listaImoveis').empty();
        if (!lista.length) { $lista.append($('<div>').addClass('list-empty').text('Nenhum imóvel encontrado.')); return; }
        lista.forEach(function (i) {
            var id = i.idImovel;
            var item = $('<div>').addClass('list-item').toggleClass('selected', id === idSelecionado).on('click', function () { selecionar(id); });
            item.append($('<div>').addClass('item-main')
                .append($('<div>').addClass('item-code').text(i.codigoImovel))
                .append($('<div>').addClass('item-title').text(i.descricao))
                .append($('<div>').addClass('item-subtitle').text((i.tipo || '') + ' • ' + (i.status || ''))))
                .append($('<span>').addClass('item-badge badge-success').text(i.finalidade || ''));
            $lista.append(item);
        });
    }

    function carregar() {
        var busca = $.trim($('#buscaImovel').val());
        return ZeroAndarApi.get('/imoveis' + (busca ? '?busca=' + encodeURIComponent(busca) : ''))
            .then(function (lista) { render(lista); return lista; });
    }

    function carregarSelects() {
        return Promise.all([ZeroAndarApi.get('/proprietarios'), ZeroAndarApi.get('/corretores')]).then(function (r) {
            var $p = $('#imovelProprietario').empty(), $c = $('#imovelCorretor').empty();
            $p.append('<option value="">Selecione o proprietário</option>');
            $c.append('<option value="">Selecione o corretor</option>');
            r[0].forEach(function (p) { $p.append($('<option>').val(p.idPessoaProprietario).text(p.nome)); });
            r[1].forEach(function (c) { $c.append($('<option>').val(c.idPessoaCorretor).text(c.nome + ' (' + c.creci + ')')); });
        });
    }

    // Limpa somente o formulário. Não usa form.reset(), porque o HTML da Etapa 8
    // possui valores estáticos e o reset faria esses valores voltarem para a tela.
    function limparFormulario() {
        idSelecionado = null;

        $('#imovelId').val('');
        $('#imovelCodigo').val('');
        $('#imovelTipo').val('');
        $('#imovelFinalidade').val('');
        $('#imovelDescricao').val('');
        $('#imovelValorVenda').val('');
        $('#imovelValorAluguel').val('');
        $('#imovelQuartos').val('');
        $('#imovelBanheiros').val('');
        $('#imovelVagas').val('');
        $('#imovelAreaTotal').val('');
        $('#imovelProprietario').val('');
        $('#imovelCorretor').val('');
        $('#imovelRua').val('');
        $('#imovelNumero').val('');
        $('#imovelComplemento').val('');
        $('#imovelBairro').val('');
        $('#imovelCidade').val('');
        $('#imovelEstado').val('');
        $('#imovelStatus').val('disponivel');
        $('#imovelFinanciamento').prop('checked', false);
        $('#imovelDestaque').prop('checked', false);

        $('#formImovelTitulo').text('Novo Imóvel');
        $('#listaImoveis .list-item').removeClass('selected');
    }

    function selecionar(id) {
        ZeroAndarApi.get('/imoveis/' + id).then(function (i) {
            idSelecionado = id;
            $('#formImovelTitulo').text('Dados do Imóvel');
            $('#imovelId').val(id); $('#imovelCodigo').val(i.codigoImovel || '');
            $('#imovelTipo').val(i.tipo || ''); $('#imovelFinalidade').val(i.finalidade || '');
            $('#imovelDescricao').val(i.descricao || ''); $('#imovelValorVenda').val(i.valorVenda || ''); $('#imovelValorAluguel').val(i.valorAluguel || '');
            $('#imovelQuartos').val(i.quartos || 0); $('#imovelBanheiros').val(i.banheiros || 0); $('#imovelVagas').val(i.vagasGaragem || 0); $('#imovelAreaTotal').val(i.areaTotal || '');
            $('#imovelProprietario').val(i.proprietario ? i.proprietario.idPessoaProprietario : ''); $('#imovelCorretor').val(i.corretorResponsavel ? i.corretorResponsavel.idPessoaCorretor : '');
            var e = i.endereco || {}; $('#imovelRua').val(e.rua || ''); $('#imovelNumero').val(e.numero || ''); $('#imovelComplemento').val(e.complemento || ''); $('#imovelBairro').val(e.bairro || ''); $('#imovelCidade').val(e.cidade || ''); $('#imovelEstado').val(e.estado || '');
            $('#imovelStatus').val(i.status || 'disponivel'); $('#imovelFinanciamento').prop('checked', !!i.aceitaFinanciamento); $('#imovelDestaque').prop('checked', !!i.destaque);
            renderSelection();
        }).catch(function (e) { alert(e.message); });
    }

    function renderSelection() { carregar(); }

    function dados() {
        return {
            // O código é gerado pelo backend para um novo imóvel.
            codigoImovel: $('#imovelCodigo').val(),
            tipo: $('#imovelTipo').val(),
            finalidade: $('#imovelFinalidade').val(),
            descricao: $.trim($('#imovelDescricao').val()),
            valorVenda: $('#imovelValorVenda').val() ? Number($('#imovelValorVenda').val()) : 0,
            valorAluguel: $('#imovelValorAluguel').val() ? Number($('#imovelValorAluguel').val()) : 0,
            quartos: Number($('#imovelQuartos').val()) || 0,
            banheiros: Number($('#imovelBanheiros').val()) || 0,
            vagasGaragem: Number($('#imovelVagas').val()) || 0,
            areaTotal: Number($('#imovelAreaTotal').val()) || 0,
            proprietarioId: $('#imovelProprietario').val(),
            corretorId: $('#imovelCorretor').val(),
            rua: $.trim($('#imovelRua').val()),
            numero: $.trim($('#imovelNumero').val()),
            complemento: $.trim($('#imovelComplemento').val()),
            bairro: $.trim($('#imovelBairro').val()),
            cidade: $.trim($('#imovelCidade').val()),
            estado: $.trim($('#imovelEstado').val()).toUpperCase(),
            status: $('#imovelStatus').val(),
            aceitaFinanciamento: $('#imovelFinanciamento').is(':checked'),
            destaque: $('#imovelDestaque').is(':checked'),
            aceitaPermuta: false,
            mobiliado: false
        };
    }

    $('#formImovel').on('submit', function (e) {
        e.preventDefault();

        var d = dados();
        var novo = !idSelecionado;

        // Validação simples no navegador antes de enviar ao Java.
        if (!d.tipo || !d.finalidade || !d.descricao || !d.proprietarioId ||
                (!d.valorVenda && !d.valorAluguel) || !d.rua || !d.bairro ||
                !d.cidade || !d.estado) {
            alert('Preencha os campos obrigatórios do imóvel.');
            return;
        }

        var url = novo ? '/imoveis' : '/imoveis/' + idSelecionado;
        var requisicao = novo ? ZeroAndarApi.post(url, d) : ZeroAndarApi.put(url, d);

        requisicao
            .then(function (imovel) {
                alert(novo ? 'Imóvel cadastrado com sucesso.' : 'Imóvel alterado com sucesso.');
                return carregarSelects().then(function () {
                    return carregar();
                }).then(function () {
                    // Após incluir/alterar, seleciona somente o registro que acabou de ser salvo.
                    if (imovel && imovel.idImovel) {
                        selecionar(imovel.idImovel);
                    }
                });
            })
            .catch(function (e) {
                alert(e.message || 'Não foi possível salvar o imóvel.');
            });
    });

    // Novo e Limpar têm a mesma regra: limpar o formulário, sem alterar a lista.
    $('#btnNovoImovel').on('click', function (e) {
        e.preventDefault();
        limparFormulario();
    });

    $('#btnLimparImovel').on('click', function (e) {
        e.preventDefault();
        limparFormulario();
    });

    $('#btnExcluirImovel').on('click', function (e) {
        e.preventDefault();

        if (!idSelecionado) {
            alert('Selecione um imóvel para excluir.');
            return;
        }

        var idExcluir = idSelecionado;
        if (!confirm('Deseja realmente excluir este imóvel?')) {
            return;
        }

        ZeroAndarApi.remove('/imoveis/' + idExcluir)
            .then(function () {
                // O formulário precisa ficar vazio imediatamente após a exclusão.
                limparFormulario();
                return carregar();
            })
            .then(function () {
                alert('Imóvel excluído com sucesso.');
            })
            .catch(function (e) {
                alert(e.message || 'Não foi possível excluir o imóvel.');
            });
    });

    // O filtro é feito pelo backend; não filtra somente os registros já desenhados.
    $('#buscaImovel').on('input', function () {
        carregar();
    });

    // Mantém a seleção inicial da versão anterior, que permite alterar/excluir.
    carregarSelects().then(carregar).then(function (lista) {
        if (lista.length) selecionar(lista[0].idImovel);
    });
});
