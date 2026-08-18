/**
 * UC15 - Projeto Integrador 3 - Etapa 8
 * @author Alex
 * @since 15 de agosto de 2026
 * @version 1.8
 *
 * Dados mockados (arrays em memoria) para simular o banco de dados
 * enquanto a Etapa 8 nao possui back-end. Os campos seguem o modelo
 * usado no script database/zeroandar_db.sql, para facilitar a troca
 * pelo acesso real ao banco na Etapa 9.
 *
 * IMPORTANTE: os dados aqui ficam apenas em memoria (variavel JS).
 * Ao recarregar a pagina, tudo volta ao estado inicial definido abaixo.
 */

var ZeroAndarMock = {

    corretores: [
        { id: 1, creci: 'CRECI-RS-12345', nome: 'Ricardo Gomes', cpf: '222.222.222-22', rg: 'RS998877',
          email: 'ricardo@zandar.com', telefone: '(54) 99999-0002', dataAdmissao: '2020-01-15',
          dataDesligamento: '', especialidade: 'Residencial', comissao: 6.00, metaMensal: 50000,
          rua: 'Rua Moron', numero: '850', cidade: 'Passo Fundo', estado: 'RS',
          observacoes: 'Corretor senior, atende principalmente a regiao central.' },
        { id: 2, creci: 'CRECI-RS-67890', nome: 'Patricia Lima', cpf: '333.333.333-33', rg: 'RS776655',
          email: 'patricia@zandar.com', telefone: '(54) 99999-0003', dataAdmissao: '2021-03-20',
          dataDesligamento: '', especialidade: 'Comercial', comissao: 6.50, metaMensal: 45000,
          rua: 'Av. Presidente Vargas', numero: '2300', cidade: 'Passo Fundo', estado: 'RS',
          observacoes: 'Especialista em imoveis comerciais.' },
        { id: 3, creci: 'CRECI-RS-11223', nome: 'Eduardo Costa', cpf: '777.777.777-77', rg: 'RS554433',
          email: 'eduardo@zandar.com', telefone: '(54) 99999-0007', dataAdmissao: '2023-06-01',
          dataDesligamento: '', especialidade: 'Residencial', comissao: 5.50, metaMensal: 30000,
          rua: 'Rua Uruguai', numero: '1200', cidade: 'Passo Fundo', estado: 'RS',
          observacoes: '' }
    ],

    proprietarios: [
        { id: 1, codigo: 'PROP-001', nome: 'João Silva', cpf: '555.555.555-55', rg: 'RS112233',
          email: 'joao.prop@email.com', telefone: '(54) 99999-0005', rua: 'Av. São Vicente', numero: '500',
          cidade: 'Passo Fundo', estado: 'RS', dataCadastro: '2024-12-01', aceitaContato: true,
          imoveisVinculados: 'IMOV-001', observacoes: '' },
        { id: 2, codigo: 'PROP-002', nome: 'Maria Oliveira', cpf: '666.666.666-66', rg: 'RS223344',
          email: 'maria.prop@email.com', telefone: '(54) 99999-0006', rua: 'Rua Paissandu', numero: '789',
          cidade: 'Passo Fundo', estado: 'RS', dataCadastro: '2024-11-15', aceitaContato: true,
          imoveisVinculados: 'IMOV-002', observacoes: '' },
        { id: 3, codigo: 'PROP-003', nome: 'Carlos Pereira', cpf: '888.888.888-88', rg: 'RS334455',
          email: 'carlos.prop@email.com', telefone: '(54) 99999-0008', rua: 'Av. Brasil', numero: '1500',
          cidade: 'Passo Fundo', estado: 'RS', dataCadastro: '2025-02-10', aceitaContato: false,
          imoveisVinculados: 'IMOV-003', observacoes: 'Prefere ser contatado por e-mail.' }
    ],

    clientes: [
        { id: 1, codigo: 'CLI-001', nome: 'Ana Santos', cpf: '444.444.444-44', rg: 'MG123456',
          email: 'ana.santos@email.com', telefone: '(54) 99999-0004', dataNascimento: '1988-11-10',
          endereco: 'Av. Brasil, 1500', cidade: 'Passo Fundo', estado: 'RS', corretorId: 1,
          tipoInteresse: 'Comprar Apartamento', ativo: true,
          observacoes: 'Cliente interessado em apartamento 3 quartos, Centro, até R$ 450.000' },
        { id: 2, codigo: 'CLI-002', nome: 'Carlos Rocha', cpf: '123.456.789-01', rg: 'RS445566',
          email: 'carlos.rocha@email.com', telefone: '(54) 99999-0002', dataNascimento: '1990-04-02',
          endereco: 'Rua Uruguai, 1200', cidade: 'Passo Fundo', estado: 'RS', corretorId: 2,
          tipoInteresse: 'Alugar Casa', ativo: true,
          observacoes: 'Procura imovel para alugar proximo ao centro.' },
        { id: 3, codigo: 'CLI-003', nome: 'Fernanda Lima', cpf: '987.654.321-00', rg: 'RS667788',
          email: 'fernanda.lima@email.com', telefone: '(54) 99999-0003', dataNascimento: '1995-08-22',
          endereco: 'Rua Paissandu', cidade: 'Passo Fundo', estado: 'RS', corretorId: 1,
          tipoInteresse: 'Comprar Casa', ativo: false,
          observacoes: 'Cadastro inativo - sem contato recente.' }
    ],

    imoveis: [
        { id: 1, codigo: 'IMOV-001', tipo: 'Apartamento', finalidade: 'venda',
          descricao: 'Apartamento 3 quartos, 2 banheiros, sacada, churrasqueira',
          valorVenda: 350000.00, valorAluguel: null, quartos: 3, banheiros: 2, vagasGaragem: 1,
          areaTotal: 85.50, proprietarioId: 1, corretorId: 1,
          rua: 'Av. Brasil', numero: '1500', complemento: '', bairro: 'Centro', cidade: 'Passo Fundo', estado: 'RS',
          aceitaFinanciamento: true, destaque: true, status: 'disponivel' },
        { id: 2, codigo: 'IMOV-002', tipo: 'Casa', finalidade: 'venda',
          descricao: 'Casa ampla no Vera Cruz, 4 quartos, quintal grande',
          valorVenda: 650000.00, valorAluguel: null, quartos: 4, banheiros: 3, vagasGaragem: 2,
          areaTotal: 220.00, proprietarioId: 2, corretorId: 1,
          rua: 'Av. Presidente Vargas', numero: '2300', complemento: '', bairro: 'Vera Cruz', cidade: 'Passo Fundo', estado: 'RS',
          aceitaFinanciamento: true, destaque: true, status: 'reservado' },
        { id: 3, codigo: 'IMOV-003', tipo: 'Comercial', finalidade: 'aluguel',
          descricao: 'Sala comercial no Centro, pronta para uso',
          valorVenda: null, valorAluguel: 2500.00, quartos: 0, banheiros: 1, vagasGaragem: 0,
          areaTotal: 45.00, proprietarioId: 3, corretorId: 2,
          rua: 'Rua Moron', numero: '850', complemento: 'Sala 2', bairro: 'Centro', cidade: 'Passo Fundo', estado: 'RS',
          aceitaFinanciamento: false, destaque: true, status: 'disponivel' },
        { id: 4, codigo: 'IMOV-004', tipo: 'Sobrado', finalidade: 'venda',
          descricao: 'Sobrado 5 quartos, otimo para familia grande',
          valorVenda: 850000.00, valorAluguel: null, quartos: 5, banheiros: 4, vagasGaragem: 3,
          areaTotal: 310.00, proprietarioId: 1, corretorId: 3,
          rua: 'Rua Uruguai', numero: '1200', complemento: '', bairro: 'Centro', cidade: 'Passo Fundo', estado: 'RS',
          aceitaFinanciamento: true, destaque: false, status: 'vendido' }
    ],

    /** Retorna o nome do corretor pelo id (usado nos selects e nos cards). */
    nomeCorretor: function (id) {
        var c = this.corretores.filter(function (x) { return x.id === Number(id); })[0];
        return c ? c.nome + ' (' + c.creci + ')' : '-';
    },

    /** Retorna o nome do proprietario pelo id. */
    nomeProprietario: function (id) {
        var p = this.proprietarios.filter(function (x) { return x.id === Number(id); })[0];
        return p ? p.nome + ' (' + p.codigo + ')' : '-';
    },

    /** Gera o proximo id sequencial de um array mockado. */
    proximoId: function (lista) {
        var max = 0;
        lista.forEach(function (item) { if (item.id > max) max = item.id; });
        return max + 1;
    }
};
