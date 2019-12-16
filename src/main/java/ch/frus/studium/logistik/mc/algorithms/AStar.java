package ch.frus.studium.logistik.mc.algorithms;

import ch.frus.studium.logistik.mc.NoPathException;
import ch.frus.studium.logistik.mc.Utils;
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

    public ArrayList<Block> run() throws InterruptedException, NoPathException {
        this.log.info("init run");

        int counter = 0;
        while(!this.openList.isEmpty()) {
            counter++;
            if (counter == this.maxIteration) {
                break;
            }

            this.log.info(ANSI_WHITE_BACKGROUND + ANSI_RED +"########################################################" + ANSI_RESET);
            this.log.info("loop-cycle:              " + counter);
            this.log.info("size openlist:           " + this.openList.size());

            Block currentBlock = getLowestFScoreBlock();
            this.log.info("lowest fScore block:     " + Utils.getLocationString(currentBlock));
            this.log.info("fscore:                  " + this.fScore.get(currentBlock));

            this.openList.remove(currentBlock);
            this.expandBlock(currentBlock);

            if (currentBlock.getLocation().equals(this.endBlock.getLocation())) {
                return getPath();
            }

            Thread.sleep(this.whileDelayMilli);
        }

//        this.cameFrom.forEach((Block from, Block to) -> from.setType(Material.GLASS));

        throw new NoPathException("There is no path between the selection.");
    }

    private void expandBlock(Block currentBlock) {
        ArrayList<Block> successors = getSuccessor(currentBlock);

        for (Block successor : successors) {
            this.log.info("Successor: " + getLocationString(successor));

            if (this.cameFrom.containsKey(successor)) {
                this.log.info("> Already handled");
                continue;
            }

            int tentativeGScore = this.gScore.get(currentBlock) + 1;
            this.log.info("tentativeGScore: " + tentativeGScore);
            this.log.info("old gScore: " + this.gScore.get(successor));
            this.log.info("tentG < gScore: " + (tentativeGScore < this.gScore.get(successor)));

            if (this.gScore.get(successor) > tentativeGScore) {
                // This path to neighbor is better than any previous one. Record it!
                this.log.info("> Record it! (Tentative smaller then gScore)");

                this.cameFrom.put(successor, currentBlock);
                this.gScore.put(successor, tentativeGScore); // TODO bug existing
                this.fScore.put(successor, getEstimatedDistance(successor, this.endBlock));

                if (!this.openList.contains(successor)) {
                    this.log.info("> Add to openlist");
                    this.openList.add(successor);
                } else {
                    this.log.info("> Already on openlist");
                }
            } else {
                this.log.info("> tentative smaller: " + tentativeGScore + " > " + this.gScore.get(successor));
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
        return (int) Math.pow(
                Math.pow(start.getX() - destination.getX(), 2)
                        + Math.pow(start.getY() - destination.getY(), 2)
                        + Math.pow(start.getZ() - destination.getZ(), 2),
                0.5d
        );
    }

    private Block getLowestFScoreBlock() {
        Block blockWithLowestFScore = null;
        double minFScore = Integer.MAX_VALUE;

        this.log.info("");
        this.log.info("### start getLowestFScoreBlock ###");

        for (Block b : openList) {
            int f = fScore.get(b);
            if (f < minFScore) {
                blockWithLowestFScore = b;
                minFScore = f;
            }
        }

        this.log.info("### end getLowestFScoreBlock ###");
        this.log.info("");

        return blockWithLowestFScore;
    }

    private ArrayList<Block> getPath() {
        Block iteratedBlock = this.endBlock;
        ArrayList<Block> path = new ArrayList<>();
        while (this.cameFrom.containsKey(iteratedBlock)) {
            path.add(iteratedBlock);

            if (iteratedBlock.equals(this.startBlock))
                break;

            iteratedBlock = this.cameFrom.get(iteratedBlock);
        }
        return path;
    }
}
