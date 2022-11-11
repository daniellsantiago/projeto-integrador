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

INSERT INTO `buyer` (`id`) VALUES(1);
INSERT INTO `buyer` (`id`) VALUES(2);
INSERT INTO `buyer` (`id`) VALUES(3);
--
-- Extraindo dados da tabela `inbound_order`
--

INSERT INTO `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) VALUES(1, '2022-11-08', 1, 1, 3);
INSERT INTO `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) VALUES(2, '2022-11-12', 6, 3, 1);
INSERT INTO `inbound_order` (`id`, `order_date`, `section_id`, `warehouse_id`, `warehouse_operator_id`) VALUES(3, '2022-11-12', 5, 2, 2);

--
-- Extraindo dados da tabela `item_batch`
--

INSERT INTO `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `storage_type`, `volume`, `inbound_order_id`, `product_id`) VALUES(1, '2022-11-11', '2022-11-08', '2022-11-08 22:27:42', '30.20', 5, 'CONGELADO', 3, 1, 1);
INSERT INTO `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `storage_type`, `volume`, `inbound_order_id`, `product_id`) VALUES(2, '2022-11-12', '2022-11-09', '2022-11-09 22:27:42', '55.20', 15, 'CONGELADO', 3, 1, 1);
INSERT INTO `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `storage_type`, `volume`, `inbound_order_id`, `product_id`) VALUES(3, '2022-11-13', '2022-11-10', '2022-11-10 22:27:42', '45.20', 7, 'CONGELADO', 3, 1, 1);
INSERT INTO `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `storage_type`, `volume`, `inbound_order_id`, `product_id`) VALUES(4, '2022-11-13', '2022-11-12', '2022-11-12 22:27:42', '75.20', 2, 'REFRIGERADO', 4, 2, 2);
INSERT INTO `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `storage_type`, `volume`, `inbound_order_id`, `product_id`) VALUES(5, '2022-11-20', '2022-11-12', '2022-11-12 22:27:42', '75.20', 9, 'REFRIGERADO', 18, 2, 2);
INSERT INTO `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `storage_type`, `volume`, `inbound_order_id`, `product_id`) VALUES(6, '2022-12-13', '2022-11-12', '2022-11-12 22:27:42', '75.20', 6, 'REFRIGERADO', 12, 2, 2);
INSERT INTO `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `storage_type`, `volume`, `inbound_order_id`, `product_id`) VALUES(7, '2022-11-13', '2022-11-10', '2022-11-10 22:27:42', '45.20', 5, 'CONGELADO', 5, 1, 6);
INSERT INTO `item_batch` (`id`, `due_date`, `manufacturing_date`, `manufacturing_time`, `price`, `product_quantity`, `storage_type`, `volume`, `inbound_order_id`, `product_id`) VALUES(8, '2022-11-13', '2022-11-10', '2022-11-10 22:27:42', '45.20', 20, 'CONGELADO', 48, 3, 1);

--
-- Extraindo dados da tabela `order_purchase`
--
INSERT INTO `order_purchase` (`id`, `date_order`, `status`) VALUES(1, '2022-11-10', 0);


--
-- Extraindo dados da tabela `product`
--

INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(1, '41.50', 'CONGELADO', 1);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(2, '32.50', 'REFRIGERADO', 1);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(3, '43.50', 'REFRIGERADO', 1);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(4, '11.50', 'FRESCO', 1);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(5, '22.50', 'REFRIGERADO', 1);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(6, '555.50', 'CONGELADO', 2);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(7, '33.50', 'FRESCO', 3);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(8, '41.50', 'FRESCO', 1);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(9, '32.50', 'CONGELADO', 1);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(10, '43.50', 'REFRIGERADO', 3);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(11, '11.50', 'FRESCO', 1);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(12, '22.50', 'REFRIGERADO', 1);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(13, '55.50', 'CONGELADO', 2);
INSERT INTO `product` (`id`, `price`, `category`, `seller_id`) VALUES(14, '33.50', 'FRESCO', 3);

--
-- Extraindo dados da tabela `product_order`
--
INSERT INTO `product_order` (`id`, `product_id`, `quantity`, `order_purchase_id`) VALUES(1, 1, 5, 1);

--
-- Extraindo dados da tabela `section`
--

INSERT INTO `section` (`id`, `storage_type`, `volume`, `warehouse_id`) VALUES(1, 'FRESCO', 6100, 1);
INSERT INTO `section` (`id`, `storage_type`, `volume`, `warehouse_id`) VALUES(2, 'CONGELADO', 4100, 1);
INSERT INTO `section` (`id`, `storage_type`, `volume`, `warehouse_id`) VALUES(3, 'REFRIGERADO', 7100, 1);
INSERT INTO `section` (`id`, `storage_type`, `volume`, `warehouse_id`) VALUES(4, 'FRESCO', 6200, 2);
INSERT INTO `section` (`id`, `storage_type`, `volume`, `warehouse_id`) VALUES(5, 'CONGELADO', 4200, 2);
INSERT INTO `section` (`id`, `storage_type`, `volume`, `warehouse_id`) VALUES(6, 'REFRIGERADO', 7300, 3);
INSERT INTO `section` (`id`, `storage_type`, `volume`, `warehouse_id`) VALUES(7, 'FRESCO', 6300, 3);

--
-- Extraindo dados da tabela `seller`
--

INSERT INTO `seller` (`id`) VALUES(1);
INSERT INTO `seller` (`id`) VALUES(2);
INSERT INTO `seller` (`id`) VALUES(3);

--
-- Extraindo dados da tabela `warehouse`
--

INSERT INTO `warehouse` (`id`, `warehouse_operator_id`) VALUES(3, 1);
INSERT INTO `warehouse` (`id`, `warehouse_operator_id`) VALUES(2, 2);
INSERT INTO `warehouse` (`id`, `warehouse_operator_id`) VALUES(1, 3);

--
-- Extraindo dados da tabela `warehouse_operator`
--

INSERT INTO `warehouse_operator` (`id`) VALUES(1);
INSERT INTO `warehouse_operator` (`id`) VALUES(2);
INSERT INTO `warehouse_operator` (`id`) VALUES(3);
SET FOREIGN_KEY_CHECKS=1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;