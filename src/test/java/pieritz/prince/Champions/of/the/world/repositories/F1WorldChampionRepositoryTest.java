package pieritz.prince.Champions.of.the.world.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pieritz.prince.Champions.of.the.world.domain.F1WorldChampion;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class F1WorldChampionRepositoryTest {

    @Autowired
    private F1WorldChampionRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        prepareTestData();
    }

    private void prepareTestData() {
        repository.save(new F1WorldChampion(null, 2020, 15, "Lewis Hamilton", "Mercedes", "GB", 35, BigDecimal.valueOf(347), 11));
        repository.save(new F1WorldChampion(null, 2021, 22, "Max Verstappen", "Red Bull Racing", "NL", 24, BigDecimal.valueOf(395), 10));
        repository.save(new F1WorldChampion(null, 2021, 20, "Valtteri Bottas", "Mercedes", "FI", 32, BigDecimal.valueOf(226), 9));
    }

    @Test
    void testFindBySeason() {
        List<F1WorldChampion> champions = repository.findBySeason(2021);
        assertEquals(2, champions.size(), "Expected two champions for the season 2021.");
        assertTrue(champions.stream().anyMatch(c -> c.getChampion().equals("Max Verstappen")), "Max Verstappen should be a champion for 2021.");
        assertTrue(champions.stream().anyMatch(c -> c.getChampion().equals("Valtteri Bottas")), "Valtteri Bottas should be a champion for 2021.");
    }

    @Test
    void testExistsByTeam() {
        assertTrue(repository.existsByTeam("Mercedes"), "Mercedes should have champions.");
        assertFalse(repository.existsByTeam("Ferrari"), "Ferrari should not have champions.");
    }

    @Test
    void testExistsByNationality() {
        assertTrue(repository.existsByNationality("GB"), "There should be a champion from GB.");
        assertFalse(repository.existsByNationality("USA"), "There should be no champion from USA.");
    }

    @Test
    void testFindByNationalityOrderBySeasonAsc() {
        List<F1WorldChampion> champions = repository.findByNationalityOrderBySeasonAsc("GB");
        assertEquals(1, champions.size(), "Expected one champion from GB.");
        assertEquals("Lewis Hamilton", champions.get(0).getChampion(), "The champion from GB should be Lewis Hamilton.");
    }

    @Test
    void testFindAllByOrderByRacesDesc() {
        List<F1WorldChampion> champions = repository.findAllByOrderByRacesDesc();
        assertEquals(3, champions.size(), "Expected three champions.");
        assertEquals("Max Verstappen", champions.get(0).getChampion(), "The champion with the most races should be Max Verstappen.");
    }

    @Test
    void testFindAllByPointsGreaterThan() {
        List<F1WorldChampion> champions = repository.findAllByPointsGreaterThan(BigDecimal.valueOf(300));
        assertEquals(2, champions.size(), "Expected two champions with points greater than 300.");
        assertTrue(champions.stream().anyMatch(c -> c.getChampion().equals("Lewis Hamilton")), "Lewis Hamilton should have more than 300 points.");
        assertTrue(champions.stream().anyMatch(c -> c.getChampion().equals("Max Verstappen")), "Max Verstappen should have more than 300 points.");
    }

    @Test
    void testFindAllByPointsLessThan() {
        List<F1WorldChampion> champions = repository.findAllByPointsLessThan(BigDecimal.valueOf(300));
        assertEquals(1, champions.size(), "Expected one champion with points less than 300.");
        assertEquals("Valtteri Bottas", champions.get(0).getChampion(), "The champion with points less than 300 should be Valtteri Bottas.");
    }

    @Test
    void testFindByTeamOrderBySeasonAsc() {
        List<F1WorldChampion> champions = repository.findByTeamOrderBySeasonAsc("Mercedes");
        assertEquals(2, champions.size(), "Expected two champions from Mercedes.");
        assertEquals("Lewis Hamilton", champions.get(0).getChampion(), "First champion from Mercedes should be Lewis Hamilton.");
        assertEquals("Valtteri Bottas", champions.get(1).getChampion(), "Second champion from Mercedes should be Valtteri Bottas.");
    }
}
