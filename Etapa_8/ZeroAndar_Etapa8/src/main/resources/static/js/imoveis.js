/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 *
 * Tela de Gestão de Imóveis: lista + formulário (master-detail).
 * Os dados vêm e voltam para o array ZeroAndarMock.imoveis (js/mock-data.js),
 * simulando persistência apenas em memória, sem back-end/banco de dados
 * (conforme o escopo da Etapa 8).
 */

$(document).ready(function () {

    var idSelecionado = null;

    var badgePorStatus = {
        'disponivel': { texto: 'Disponível', classe: 'badge-success' },
        'reservado': { texto: 'Reservado', classe: 'badge-warning' },
        'vendido': { texto: 'Vendido', classe: 'badge-danger' },
        'alugado': { texto: 'Alugado', classe: 'badge-danger' },
        'inativo': { texto: 'Inativo', classe: 'badge-warning' }
    };

    function popularSelects() {
        var $prop = $('#imovelProprietario').empty();
        $prop.append($('<option>').val('').text('Selecione o proprietário'));
        ZeroAndarMock.proprietarios.forEach(function (p) {
            $prop.append($('<option>').val(p.id).text(p.nome + ' (' + p.codigo + ')'));
        });

        var $cor = $('#imovelCorretor').empty();
        $cor.append($('<option>').val('').text('Selecione o corretor'));
        ZeroAndarMock.corretores.forEach(function (c) {
            $cor.append($('<option>').val(c.id).text(c.nome + ' (' + c.creci + ')'));
        });
    }

    function formatarMoeda(valor) {
        return valor ? 'R$ ' + Number(valor).toLocaleString('pt-BR', { minimumFractionDigits: 2 }) : '';
    }

    function renderizarLista(filtro) {
        var $lista = $('#listaImoveis').empty();
        var termo = (filtro || '').toLowerCase();

        var itens = ZeroAndarMock.imoveis.filter(function (imovel) {
            if (!termo) return true;
            var alvo = (imovel.codigo + ' ' + imovel.tipo + ' ' + imovel.rua + ' ' + imovel.bairro).toLowerCase();
            return alvo.indexOf(termo) !== -1;
        });

        if (itens.length === 0) {
            $lista.append($('<div>').addClass('list-empty').text('Nenhum imóvel encontrado.'));
            return;
        }

        itens.forEach(function (imovel) {
            var badge = badgePorStatus[imovel.status] || { texto: imovel.status, classe: 'badge-success' };
            var precoTexto = imovel.valorVenda
                ? formatarMoeda(imovel.valorVenda)
                : (imovel.valorAluguel ? formatarMoeda(imovel.valorAluguel) + '/mês' : 'Sob consulta');

            var $item = $('<div>').addClass('list-item').attr('data-id', imovel.id);
            if (imovel.id === idSelecionado) $item.addClass('selected');

            var $main = $('<div>').addClass('item-main');
            $main.append($('<div>').addClass('item-code').text(imovel.codigo));
            $main.append($('<div>').addClass('item-title').text(imovel.tipo + ' - ' + imovel.descricao.split(',')[0]));
            $main.append($('<div>').addClass('item-subtitle').text(imovel.rua + ', ' + imovel.numero + ' - ' + imovel.bairro + ' • ' + precoTexto));

            $item.append($main);
            $item.append($('<span>').addClass('item-badge ' + badge.classe).text(badge.texto));

            $item.on('click', function () {
                selecionarImovel(imovel.id);
            });

            $lista.append($item);
        });
    }

    function limparFormulario() {
        idSelecionado = null;
        $('#formImovel')[0].reset();
        $('#imovelId').val('');
        $('#imovelCodigo').val('(gerado ao salvar)');
        $('#imovelCidade').val('Passo Fundo');
        $('#imovelEstado').val('RS');
        $('#formImovelTitulo').text('Novo Imóvel');
        $('.is-invalid').removeClass('is-invalid');
        renderizarLista($('#buscaImovel').val());
    }

    function selecionarImovel(id) {
        var imovel = ZeroAndarMock.imoveis.filter(function (i) { return i.id === id; })[0];
        if (!imovel) return;

        idSelecionado = id;
        $('#formImovelTitulo').text('Dados do Imóvel');
        $('#imovelId').val(imovel.id);
        $('#imovelCodigo').val(imovel.codigo);
        $('#imovelTipo').val(imovel.tipo);
        $('#imovelFinalidade').val(imovel.finalidade);
        $('#imovelDescricao').val(imovel.descricao);
        $('#imovelValorVenda').val(imovel.valorVenda || '');
        $('#imovelValorAluguel').val(imovel.valorAluguel || '');
        $('#imovelQuartos').val(imovel.quartos);
        $('#imovelBanheiros').val(imovel.banheiros);
        $('#imovelVagas').val(imovel.vagasGaragem);
        $('#imovelAreaTotal').val(imovel.areaTotal || '');
        $('#imovelProprietario').val(imovel.proprietarioId);
        $('#imovelCorretor').val(imovel.corretorId || '');
        $('#imovelRua').val(imovel.rua);
        $('#imovelNumero').val(imovel.numero);
        $('#imovelComplemento').val(imovel.complemento);
        $('#imovelBairro').val(imovel.bairro);
        $('#imovelCidade').val(imovel.cidade);
        $('#imovelEstado').val(imovel.estado);
        $('#imovelStatus').val(imovel.status);
        $('#imovelFinanciamento').prop('checked', imovel.aceitaFinanciamento);
        $('#imovelDestaque').prop('checked', imovel.destaque);

        $('.is-invalid').removeClass('is-invalid');
        renderizarLista($('#buscaImovel').val());
    }

    function validarFormulario() {
        var valido = true;
        var campos = [
            { campo: '#imovelDescricao', obrigatorio: true },
            { campo: '#imovelProprietario', obrigatorio: true },
            { campo: '#imovelRua', obrigatorio: true }
        ];

        campos.forEach(function (item) {
            var $campo = $(item.campo);
            var vazio = !$.trim($campo.val());
            $campo.toggleClass('is-invalid', item.obrigatorio && vazio);
            if (item.obrigatorio && vazio) valido = false;
        });

        var valorVenda = $('#imovelValorVenda').val();
        var valorAluguel = $('#imovelValorAluguel').val();
        if (!valorVenda && !valorAluguel) {
            $('#imovelValorVenda, #imovelValorAluguel').addClass('is-invalid');
            valido = false;
        } else {
            $('#imovelValorVenda, #imovelValorAluguel').removeClass('is-invalid');
        }

        return valido;
    }

    $('#formImovel').on('submit', function (event) {
        event.preventDefault();
        if (!validarFormulario()) {
            alert('Preencha os campos obrigatórios em destaque (descrição, proprietário, rua e ao menos um valor de venda ou aluguel).');
            return;
        }

        var dados = {
            tipo: $('#imovelTipo').val(),
            finalidade: $('#imovelFinalidade').val(),
            descricao: $.trim($('#imovelDescricao').val()),
            valorVenda: $('#imovelValorVenda').val() ? Number($('#imovelValorVenda').val()) : null,
            valorAluguel: $('#imovelValorAluguel').val() ? Number($('#imovelValorAluguel').val()) : null,
            quartos: Number($('#imovelQuartos').val()) || 0,
            banheiros: Number($('#imovelBanheiros').val()) || 0,
            vagasGaragem: Number($('#imovelVagas').val()) || 0,
            areaTotal: $('#imovelAreaTotal').val() ? Number($('#imovelAreaTotal').val()) : null,
            proprietarioId: Number($('#imovelProprietario').val()),
            corretorId: $('#imovelCorretor').val() ? Number($('#imovelCorretor').val()) : null,
            rua: $.trim($('#imovelRua').val()),
            numero: $('#imovelNumero').val(),
            complemento: $('#imovelComplemento').val(),
            bairro: $('#imovelBairro').val(),
            cidade: $('#imovelCidade').val(),
            estado: $('#imovelEstado').val(),
            status: $('#imovelStatus').val(),
            aceitaFinanciamento: $('#imovelFinanciamento').is(':checked'),
            destaque: $('#imovelDestaque').is(':checked')
        };

        if (idSelecionado) {
            var indice = ZeroAndarMock.imoveis.findIndex(function (i) { return i.id === idSelecionado; });
            dados.id = idSelecionado;
            dados.codigo = ZeroAndarMock.imoveis[indice].codigo;
            ZeroAndarMock.imoveis[indice] = dados;
        } else {
            dados.id = ZeroAndarMock.proximoId(ZeroAndarMock.imoveis);
            dados.codigo = 'IMOV-' + String(dados.id).padStart(3, '0');
            ZeroAndarMock.imoveis.push(dados);
        }

        idSelecionado = dados.id;
        renderizarLista($('#buscaImovel').val());
        selecionarImovel(dados.id);
        alert('Imóvel salvo com sucesso (em memória, sem persistência em banco nesta etapa).');
    });

    $('#btnNovoImovel').on('click', limparFormulario);
    $('#btnLimparImovel').on('click', limparFormulario);

    $('#btnExcluirImovel').on('click', function () {
        if (!idSelecionado) {
            alert('Selecione um imóvel na lista para excluir.');
            return;
        }
        if (!confirm('Deseja realmente excluir este imóvel?')) return;

        ZeroAndarMock.imoveis = ZeroAndarMock.imoveis.filter(function (i) { return i.id !== idSelecionado; });
        limparFormulario();
    });

    $('#buscaImovel').on('input', function () {
        renderizarLista($(this).val());
    });

    // Inicializacao da tela
    ZeroAndarApp.inicializar(function () {
        popularSelects();
        renderizarLista();

        // Se veio de um link com ?id=, seleciona o imovel correspondente
        // (usado pelo card de "Imoveis em Destaque" do Dashboard).
        var idParametro = new URLSearchParams(window.location.search).get('id');
        var imovelInicial = idParametro ? Number(idParametro) : (ZeroAndarMock.imoveis[0] && ZeroAndarMock.imoveis[0].id);

        if (imovelInicial) {
            selecionarImovel(imovelInicial);
        } else {
            limparFormulario();
        }
    });
});
