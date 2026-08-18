/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 *
 * Preenche os cards de estatística e a lista de imóveis em destaque do
 * Dashboard a partir dos arrays mockados de js/mock-data.js (sem banco).
 */

$(document).ready(function () {

    ZeroAndarApp.inicializar(function () {

        var imoveis = ZeroAndarMock.imoveis;
        var clientes = ZeroAndarMock.clientes;

        $('#statTotalImoveis').text(imoveis.length);
        $('#statClientesAtivos').text(clientes.filter(function (c) { return c.ativo; }).length);
        $('#statCorretores').text(ZeroAndarMock.corretores.length);
        $('#statProprietarios').text(ZeroAndarMock.proprietarios.length);

        var iconesPorTipo = {
            'Apartamento': '🏠',
            'Casa': '🏡',
            'Sobrado': '🏘️',
            'Comercial': '🏢',
            'Terreno': '🌳'
        };

        var badgePorStatus = {
            'disponivel': { texto: 'Disponível', classe: 'badge-success' },
            'reservado': { texto: 'Reservado', classe: 'badge-warning' },
            'vendido': { texto: 'Vendido', classe: 'badge-success' },
            'alugado': { texto: 'Alugado', classe: 'badge-success' },
            'inativo': { texto: 'Inativo', classe: 'badge-warning' }
        };

        var destaques = imoveis.filter(function (i) { return i.destaque; });
        if (destaques.length === 0) {
            destaques = imoveis.slice(0, 3);
        }

        var $grid = $('#propertyGrid');
        $grid.empty();

        destaques.forEach(function (imovel) {
            var preco = imovel.valorVenda
                ? 'R$ ' + imovel.valorVenda.toLocaleString('pt-BR')
                : (imovel.valorAluguel ? 'R$ ' + imovel.valorAluguel.toLocaleString('pt-BR') + '/mês' : 'Sob consulta');
            var badge = badgePorStatus[imovel.status] || { texto: imovel.status, classe: 'badge-success' };
            var icone = iconesPorTipo[imovel.tipo] || '🏠';

            var card = $('<div>').addClass('property-card').attr('data-id', imovel.id);
            card.append($('<div>').addClass('property-image').text(icone));

            var info = $('<div>').addClass('property-info');
            var topo = $('<div>');
            topo.append($('<div>').addClass('property-title').text(imovel.descricao.split(',')[0]));
            topo.append($('<div>').addClass('property-address').text(imovel.rua + ', ' + imovel.numero + ' - ' + imovel.bairro));
            info.append(topo);

            var rodape = $('<div>').addClass('property-footer');
            rodape.append($('<div>').addClass('property-price').text(preco));
            rodape.append($('<span>').addClass('property-badge ' + badge.classe).text(badge.texto));
            info.append(rodape);

            card.append(info);
            card.on('click', function () {
                window.location.href = 'imoveis.html?id=' + imovel.id;
            });

            $grid.append(card);
        });
    });
});
