-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 10-Nov-2022 às 15:46
-- Versão do servidor: 10.4.20-MariaDB
-- versão do PHP: 7.3.29

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `projeto_integrador`
--

--
-- Extraindo dados da tabela `buyer`
--

insert into `buyer` (`id`) values(1);
insert into `buyer` (`id`) values(2);
insert into `buyer` (`id`) values(3);
insert into `buyer` (`id`) values(4);
insert into `buyer` (`id`) values(5);
insert into `buyer` (`id`) values(6);
insert into `buyer` (`id`) values(7);
insert into `buyer` (`id`) values(8);
insert into `buyer` (`id`) values(9);
insert into `buyer` (`id`) values(10);

--
-- Extraindo dados da tabela `seller`
--

insert into `seller` (`id`) values(1);
insert into `seller` (`id`) values(2);
insert into `seller` (`id`) values(3);
insert into `seller` (`id`) values(4);
insert into `seller` (`id`) values(5);
insert into `seller` (`id`) values(6);
insert into `seller` (`id`) values(7);
insert into `seller` (`id`) values(8);
insert into `seller` (`id`) values(9);
insert into `seller` (`id`) values(10);

--
-- Extraindo dados da tabela `warehouse_operator`
--

insert into `warehouse_operator` (`id`) values(1);
insert into `warehouse_operator` (`id`) values(2);
insert into `warehouse_operator` (`id`) values(3);
insert into `warehouse_operator` (`id`) values(4);
insert into `warehouse_operator` (`id`) values(5);
insert into `warehouse_operator` (`id`) values(6);
insert into `warehouse_operator` (`id`) values(7);
insert into `warehouse_operator` (`id`) values(8);
insert into `warehouse_operator` (`id`) values(9);
insert into `warehouse_operator` (`id`) values(10);

--
-- Extraindo dados da tabela `warehouse`
--

insert into `warehouse` (`id`, `warehouse_operator_id`) values(1, 10);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(2, 9);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(3, 8);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(4, 7);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(5, 6);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(6, 5);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(7, 4);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(8, 3);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(9, 2);
insert into `warehouse` (`id`, `warehouse_operator_id`) values(10, 1);

--
-- Extraindo dados da tabela `product`
--


insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(1, '1.75', 'CONGELADO', 1, 1, 1.5, 1);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(2, '2.30', 'REFRIGERADO', 1, 1.5, 0.5, 2);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(3, '3.90', 'REFRIGERADO', 1, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(4, '4.10', 'FRESCO', 2, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(5, '5.50', 'REFRIGERADO', 2, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(6, '6.10', 'CONGELADO', 3, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(7, '7.99', 'FRESCO', 3, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(8, '8.45', 'FRESCO', 1, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(9, '9.12', 'CONGELADO', 1, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(10, '10.20', 'REFRIGERADO', 3, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(11, '11.25', 'FRESCO', 4, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(12, '12.44', 'REFRIGERADO', 5, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(13, '13.00', 'CONGELADO', 4, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(14, '14.20', 'FRESCO', 5, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(15, '15.60', 'REFRIGERADO', 6, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(16, '6.23', 'FRESCO', 6, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(17, '7.10', 'REFRIGERADO', 6, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(18, '8.25', 'CONGELADO', 8, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(19, '9.20', 'FRESCO', 8, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(20, '2.00', 'CONGELADO', 9, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(21, '2.10', 'FRESCO', 9, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(22, '7.20', 'CONGELADO', 2, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(23, '3.30', 'FRESCO', 10, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(24, '4.00', 'CONGELADO', 10, 0, 0, 0);
insert into `product` (`id`, `price`, `category`, `seller_id`, `ite_height`, `ite_length`, `ite_width`) values(25, '14.20', 'FRESCO', 10, 0, 0, 0);

--
-- Extraindo dados da tabela `section`
--

insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(1, 'FRESCO', 6100, 1);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(2, 'CONGELADO', 4100, 1);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(3, 'REFRIGERADO', 7100, 1);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(4, 'FRESCO', 6200, 2);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(5, 'CONGELADO', 4200, 2);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(6, 'REFRIGERADO', 7300, 3);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(7, 'FRESCO', 3000, 3);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(8, 'FRESCO', 2300, 4);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(9, 'CONGELADO', 1000, 4);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(10, 'REFRIGERADO', 5500, 4);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(11, 'FRESCO', 6200, 5);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(12, 'CONGELADO', 4200, 6);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(13, 'REFRIGERADO', 7300, 6);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(14, 'FRESCO', 7800, 7);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(15, 'FRESCO', 1800, 8);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(16, 'CONGELADO', 2000, 8);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(17, 'REFRIGERADO', 2300, 9);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(18, 'FRESCO', 4100, 10);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(19, 'CONGELADO', 5400, 10);
insert into `section` (`id`, `category`, `volume`, `warehouse_id`) values(20, 'REFRIGERADO', 1900, 10);

--
-- Extraindo dados da tabela `inbound_order`
--

insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(1, '2022-11-08', 1, 1, 10);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(2, '2022-11-08', 1, 1, 10);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(3, '2022-11-08', 2, 1, 10);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(4, '2022-11-08', 4, 2, 9);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(5, '2022-11-08', 6, 3, 8);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(6, '2022-11-08', 10, 4, 7);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(7, '2022-11-08', 11, 5, 6);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(8, '2022-11-08', 12, 6, 5);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(9, '2022-11-08', 14, 7, 4);
insert into `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) values(10, '2022-11-08', 17, 9, 2);

--
-- Extraindo dados da tabela `item_batch`
--

insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(1, '2023-01-11', '2022-11-08', '2022-11-08 22:27:42', '30.20', 5, 'CONGELADO', 3, 1, 1);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(2, '2022-12-12', '2022-11-09', '2022-11-09 22:27:42', '55.20', 13, 'CONGELADO', 3, 1, 1);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(3, '2023-01-13', '2022-11-10', '2022-11-10 22:27:42', '45.20', 7, 'CONGELADO', 3, 1, 1);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(4, '2022-12-10', '2022-11-10', '2022-11-10 21:25:00', '50.00', 20, 'FRESCO', 21, 2, 4);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(5, '2022-12-30', '2022-11-12', '2022-11-12 22:27:42', '75.20', 9, 'FRESCO', 18, 2, 4);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(6, '2022-12-13', '2022-11-12', '2022-11-12 22:27:42', '75.20', 6, 'FRESCO', 12, 2, 4);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(7, '2022-12-18', '2022-11-10', '2022-11-10 22:27:42', '45.20', 5, 'CONGELADO', 5, 1, 6);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(8, '2022-12-30', '2022-11-10', '2022-11-10 22:27:42', '85.20', 20, 'CONGELADO', 48, 3, 1);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(9, '2022-12-30', '2022-11-10', '2022-11-10 22:27:42', '5.20', 6, 'CONGELADO', 5, 3, 6);
insert into `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `category`, `volume`, `inbound_order_id`, `product_id`) values(10, '2022-11-20', '2022-11-10', '2022-11-10 22:27:42', '521.20', 15, 'CONGELADO', 5, 3, 6);

--
-- Extraindo dados da tabela `order_purchase`
--
insert into `order_purchase` (`id`, `date_order`, `status`, `buyer_id`) values(1, '2022-11-10', 0, 1);
insert into `order_purchase` (`id`, `date_order`, `status`, `buyer_id`) values(2, '2022-11-10', 0, 2);

--
-- Extraindo dados da tabela `product_order`
--
insert into `product_order` (`id`, `product_id`, `quantity`, `order_purchase_id`) values(1, 1, 2, 1);
insert into `product_order` (`id`, `product_id`, `quantity`, `order_purchase_id`) values(2, 1, 1, 2);

SET FOREIGN_KEY_CHECKS=1;
commit;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;