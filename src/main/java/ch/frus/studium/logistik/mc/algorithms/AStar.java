package ch.frus.studium.logistik.mc.algorithms;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class AStar extends Algorithm {
    private final Logger log = getLogger();

    @SuppressWarnings("FieldCanBeLocal")
    private final Integer maxIteration = 100;
    @SuppressWarnings("FieldCanBeLocal")
    private final Integer whileDelayMilli = 100;

    @SuppressWarnings("FieldCanBeLocal")
    private Block startBlock;
    private Block endBlock;

    private ArrayList<Block> openList = new ArrayList<>();
    private ArrayList<Block> closedList = new ArrayList<>();

    // Current Block, Successor
    private HashMap<Block, Block> cameFrom = new HashMap<>();

    // gScore[n] is the cost of the cheapest path from start to n currently known
    private HashMap<Block, Integer> gScore = new HashMap<>();

    // best estimate of the cost of the path going through the current node
    // fScore[n] := gScore[n] + h(n)
    private HashMap<Block, Integer> fScore = new HashMap<>();


    public AStar(Block startBlock, Block endBlock) {
        this.startBlock = startBlock;
        this.endBlock = endBlock;

        this.openList.add(startBlock);
        this.gScore.put(this.startBlock, 0);
        this.fScore.put(this.startBlock, getEstimatedDistance(this.startBlock, this.endBlock));
    }

    public ArrayList<Block> run() throws InterruptedException {
        ArrayList<Block> path = new ArrayList<>();

        this.log.info("init run");

        int counter = 0;
        while(!this.openList.isEmpty()) {
            if (counter == this.maxIteration) {
                break;
            }

            Block currentBlock = getLowestFScoreBlock();

            if (currentBlock == this.endBlock) {
                Block i = this.startBlock;
                while (this.cameFrom.containsKey(i)) {
                    path.add(i);
                    i = this.cameFrom.get(i);
                }

                return path;
            }

            this.openList.remove(currentBlock);
            this.expandBlock(currentBlock);

            Thread.sleep(this.whileDelayMilli);
            counter++;
        }

        return path;
    }

    private void expandBlock(Block currentBlock) {
        ArrayList<Block> successors = getSuccessor(currentBlock);

        for (Block successor : successors) {
            int tentativeGScore = this.gScore.get(currentBlock) + 1;

            if (this.gScore.get(successor) > tentativeGScore) {
                // This path to neighbor is better than any previous one. Record it!

                this.cameFrom.put(currentBlock, successor);
                this.gScore.put(successor, tentativeGScore);
                this.fScore.put(successor, getEstimatedDistance(successor, this.endBlock));

                if (!this.openList.contains(successor)) {
                    this.openList.add(successor);
                }
            }
        }
    }

    private ArrayList<Block> getSuccessor(Block block) {
        ArrayList<Block> successors = new ArrayList<Block>();

        successors.add(block.getRelative(0,0,-1)); // Get North
        successors.add(block.getRelative(-1,0,0)); // Get West
        successors.add(block.getRelative(0,0,1)); // Get South
        successors.add(block.getRelative(1,0,0)); // Get East

        // Get NW
        // Get SW
        // Get SE
        // Get NE

        // Get High + N NW W SW S SE E NE
        // Get Low + N NW W SW S SE E NE

        // Check if they are air
            // Else check one block above (jump)

        successors.forEach((Block b) -> this.gScore.put(b, Integer.MAX_VALUE));
        return successors;
    }

    private int getEstimatedDistance(Block start, Block destination) {
//        return Math.pow(
//                Math.pow(start.getX() - destination.getX(), 2)
//                        + Math.pow(start.getY() - destination.getY(), 2)
//                        + Math.pow(start.getZ() - destination.getZ(), 2),
//                0.5d
//        );
        return (start.getX() - destination.getX()) + (start.getY() - destination.getY());
        // + (start.getZ() - destination.getZ())
    }

    private Block getLowestFScoreBlock() {
        Block blockWithLowestFScore = null;
        double minFScore = Integer.MAX_VALUE;

        for (Block b : openList) {
            if (fScore.get(b) < minFScore) {
                blockWithLowestFScore = b;
            }
        }

        return blockWithLowestFScore;
    }
}
