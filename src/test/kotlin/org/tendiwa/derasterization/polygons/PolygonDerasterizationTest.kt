package org.tendiwa.derasterization.polygons

import org.junit.Test
import org.tendiwa.graphs.edges
import org.tendiwa.grid.constructors.GridRectangle
import org.tendiwa.grid.dimensions.by
import org.tendiwa.grid.masks.*
import org.tendiwa.grid.rectangles.hulls.GridRectangularHull
import org.tendiwa.plane.geometry.graphs.constructors.Graph2D
import kotlin.test.assertEquals

class PolygonDerasterizationTest {
    @Test fun collapsesContinuousEdgeChains() {
        GridRectangle(10 by 10)
            .derasterized
            .first()
            .apply { assertEquals(4, segments.size) }
    }

    @Test fun derasterizesGridMaskWithMultipleConnectivityComponents() {
        val component1 = GridRectangle(10 by 10)
        val component2 = component1.move(20, 20)
        component1
            .union(component2)
            .boundedBy(GridRectangularHull(component1, component2))
            .derasterized
            .apply {
                assertEquals(2, size)
            }
    }

    @Test fun rejectsExtraTiles() {
        val complex =
            StringGridMask(
                ".....###..",
                ".....###..",
                ".....###.#",
                ".....##..#",
                "..##..####",
                "##########",
                "#####.####",
                ".####.####",
                ".####.#.##",
                "......####"
            )
        val simplified =
            StringGridMask(
                ".....###..",
                ".....###..",
                ".....###..",
                "..........",
                "......####",
                ".####.####",
                ".####.####",
                ".####.####",
                ".####.....",
                ".........."
            )
        assertEquals(
            Graph2D(simplified.derasterized).edges,
            Graph2D(complex.derasterized).edges
        )
    }

    @Test fun derasterizesEmptyMask() {
        EmptyGridMask()
            .boundedBy(GridRectangle(3 by 4))
            .derasterized
            .apply { assertEquals(0, size) }
    }
}
