package edu.uiowa.slis.graphtaglib.CommunityDetection;

/**
 * ModularityOptimizer
 *
 * @author Ludo Waltman
 * @author Nees Jan van Eck
 * @version 1.2.0, 05/14/14
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class ModularityOptimizer {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException {
		boolean printOutput, update;
		Console console;
		double modularity, maxModularity, resolution, resolution2;
		int algorithm, i, j, modularityFunction, nClusters, nIterations, nRandomStarts;
		int[] cluster;
		long beginTime, endTime, randomSeed;
		Network network;
		Random random;
		String inputFileName, outputFileName;

		if (args.length == 9) {
			inputFileName = args[0];
			outputFileName = args[1];
			modularityFunction = Integer.parseInt(args[2]);
			resolution = Double.parseDouble(args[3]);
			algorithm = Integer.parseInt(args[4]);
			nRandomStarts = Integer.parseInt(args[5]);
			nIterations = Integer.parseInt(args[6]);
			randomSeed = Long.parseLong(args[7]);
			printOutput = (Integer.parseInt(args[8]) > 0);

			if (printOutput) {
				System.out.println("Modularity Optimizer version 1.2.0 by Ludo Waltman and Nees Jan van Eck");
				System.out.println();
			}
		} else {
			console = System.console();
			System.out.println("Modularity Optimizer version 1.2.0 by Ludo Waltman and Nees Jan van Eck");
			System.out.println();
			inputFileName = console.readLine("Input file name: ");
			outputFileName = console.readLine("Output file name: ");
			modularityFunction = Integer
					.parseInt(console.readLine("Modularity function (1 = standard; 2 = alternative): "));
			resolution = Double.parseDouble(console.readLine("Resolution parameter (e.g., 1.0): "));
			algorithm = Integer.parseInt(console.readLine(
					"Algorithm (1 = Louvain; 2 = Louvain with multilevel refinement; 3 = smart local moving): "));
			nRandomStarts = Integer.parseInt(console.readLine("Number of random starts (e.g., 10): "));
			nIterations = Integer.parseInt(console.readLine("Number of iterations (e.g., 10): "));
			randomSeed = Long.parseLong(console.readLine("Random seed (e.g., 0): "));
			printOutput = (Integer.parseInt(console.readLine("Print output (0 = no; 1 = yes): ")) > 0);
			System.out.println();
		}

		if (printOutput) {
			System.out.println("Reading input file...");
			System.out.println();
		}

		network = readInputFile(inputFileName, modularityFunction);

		if (printOutput) {
			System.out.format("Number of nodes: %d%n", network.getNNodes());
			System.out.format("Number of edges: %d%n", network.getNEdges() / 2);
			System.out.println();
			System.out.println("Running " + ((algorithm == 1) ? "Louvain algorithm"
					: ((algorithm == 2) ? "Louvain algorithm with multilevel refinement"
							: "smart local moving algorithm"))
					+ "...");
			System.out.println();
		}

		resolution2 = ((modularityFunction == 1) ? (resolution / network.getTotalEdgeWeight()) : resolution);

		beginTime = System.currentTimeMillis();
		cluster = null;
		nClusters = -1;
		maxModularity = Double.NEGATIVE_INFINITY;
		random = new Random(randomSeed);
		for (i = 0; i < nRandomStarts; i++) {
			if (printOutput && (nRandomStarts > 1))
				System.out.format("Random start: %d%n", i + 1);

			network.initSingletonClusters();

			j = 0;
			update = true;
			do {
				if (printOutput && (nIterations > 1))
					System.out.format("Iteration: %d%n", j + 1);

				// commented out to prevent compilation errors against modified
				// Network class
				// if (algorithm == 1)
				// update = network.runLouvainAlgorithm(resolution2, random);
				// else if (algorithm == 2)
				// update =
				// network.runLouvainAlgorithmWithMultilevelRefinement(resolution2,
				// random);
				// else if (algorithm == 3)
				// network.runSmartLocalMovingAlgorithm(resolution2, random);
				j++;

				modularity = network.calcQualityFunction(resolution2);

				if (printOutput && (nIterations > 1))
					System.out.format("Modularity: %.4f%n", modularity);
			} while ((j < nIterations) && update);

			if (modularity > maxModularity) {
				network.orderClustersByNNodes();
				cluster = network.getClusters();
				nClusters = network.getNClusters();
				maxModularity = modularity;
			}

			if (printOutput && (nRandomStarts > 1)) {
				if (nIterations == 1)
					System.out.format("Modularity: %.4f%n", modularity);
				System.out.println();
			}
		}
		endTime = System.currentTimeMillis();

		if (printOutput) {
			if (nRandomStarts == 1) {
				if (nIterations > 1)
					System.out.println();
				System.out.format("Modularity: %.4f%n", maxModularity);
			} else
				System.out.format("Maximum modularity in %d random starts: %.4f%n", nRandomStarts, maxModularity);
			System.out.format("Number of communities: %d%n", nClusters);
			System.out.format("Elapsed time: %d seconds%n", Math.round((endTime - beginTime) / 1000.0));
			System.out.println();
			System.out.println("Writing output file...");
			System.out.println();
		}

		writeOutputFile(outputFileName, cluster);
	}

	public static Network readInputFile(String fileName, int modularityFunction) throws IOException {
		BufferedReader bufferedReader;
		String buffer;
		String[] splittedLine;
		Vector<Integer> node1Vector = new Vector<Integer>();
		Vector<Integer> node2Vector = new Vector<Integer>();
		Vector<Double> edgeWeightVector = new Vector<Double>();

		bufferedReader = new BufferedReader(new FileReader(fileName));

		while ((buffer = bufferedReader.readLine()) != null) {
			splittedLine = buffer.split("\t");
			node1Vector.add(Integer.parseInt(splittedLine[0]));
			node2Vector.add(Integer.parseInt(splittedLine[1]));
			edgeWeightVector.add((splittedLine.length > 2) ? Double.parseDouble(splittedLine[2]) : 1);
			Detector.logger.info("node1: " + node1Vector.lastElement() + " node2: " + node2Vector.lastElement()
					+ " weight: " + edgeWeightVector.lastElement());
		}

		bufferedReader.close();

		return convertVectors(modularityFunction, node1Vector, node2Vector, edgeWeightVector);
	}

	public static Network convertVectors(int modularityFunction, Vector<Integer> node1Vector,
			Vector<Integer> node2Vector, Vector<Double> edgeWeightVector) {
		int[] firstNeighborIndex, neighbor, nNeighbors, node1, node2;
		double[] edgeWeight1, edgeWeight2, nodeWeight;
		int nNodes, nEdges;
		int i, j;
		Network network;

		node1 = new int[node1Vector.size()];
		node2 = new int[node2Vector.size()];
		edgeWeight1 = new double[edgeWeightVector.size()];
		nNodes = -1;
		for (j = 0; j < node1Vector.size(); j++) {
			node1[j] = node1Vector.elementAt(j);
			node2[j] = node2Vector.elementAt(j);

			nNodes = Math.max(nNodes, Math.max(node1[j], node2[j]));

			edgeWeight1[j] = edgeWeightVector.elementAt(j);
		}
		nNodes++;

		nNeighbors = new int[nNodes];
		for (i = 0; i < node1Vector.size(); i++)
			if (node1[i] < node2[i]) {
				nNeighbors[node1[i]]++;
				nNeighbors[node2[i]]++;
			}

		firstNeighborIndex = new int[nNodes + 1];
		nEdges = 0;
		for (i = 0; i < nNodes; i++) {
			firstNeighborIndex[i] = nEdges;
			nEdges += nNeighbors[i];
		}
		firstNeighborIndex[nNodes] = nEdges;

		neighbor = new int[nEdges];
		edgeWeight2 = new double[nEdges];
		Arrays.fill(nNeighbors, 0);
		for (i = 0; i < node1Vector.size(); i++)
			if (node1[i] < node2[i]) {
				j = firstNeighborIndex[node1[i]] + nNeighbors[node1[i]];
				neighbor[j] = node2[i];
				edgeWeight2[j] = edgeWeight1[i];
				nNeighbors[node1[i]]++;
				j = firstNeighborIndex[node2[i]] + nNeighbors[node2[i]];
				neighbor[j] = node1[i];
				edgeWeight2[j] = edgeWeight1[i];
				nNeighbors[node2[i]]++;
			}

		if (modularityFunction == 1) {
			nodeWeight = new double[nNodes];
			for (i = 0; i < nEdges; i++)
				nodeWeight[neighbor[i]] += edgeWeight2[i];
			network = new Network(nNodes, firstNeighborIndex, neighbor, edgeWeight2, nodeWeight);
		} else
			network = new Network(nNodes, firstNeighborIndex, neighbor, edgeWeight2);

		return network;
	}

	private static void writeOutputFile(String fileName, int[] cluster) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
		int i;

		for (i = 0; i < cluster.length; i++) {
			bufferedWriter.write(Integer.toString(cluster[i]));
			bufferedWriter.newLine();
		}

		bufferedWriter.close();
	}
}
