"""
Integration tests for the Pricing Engine.
These tests can be run after building the JAR file.

Prerequisites:
    pip install pytest requests

Usage:
    pytest src/test/python/test_pricing_integration.py -v
"""

import subprocess
import json
import sys
from pathlib import Path

class PricingEngineIntegration:
    """Helper class to test the compiled PricingEngine"""
    
    def __init__(self, jar_path=None):
        if jar_path is None:
            jar_path = Path(__file__).parent.parent.parent.parent / "build" / "libs" / "pricing-discount-engine-1.0.0.jar"
        self.jar_path = str(jar_path)
    
    def test_jar_exists(self):
        """Verify the JAR file was built"""
        jar_file = Path(self.jar_path)
        assert jar_file.exists(), f"JAR file not found at {self.jar_path}. Run 'gradle build' first."
    
    def run_command(self, command):
        """Run a Java command and capture output"""
        try:
            result = subprocess.run(command, capture_output=True, text=True, shell=True)
            return result.returncode, result.stdout, result.stderr
        except Exception as e:
            print(f"Error running command: {e}")
            return -1, "", str(e)


def test_jar_compilation():
    """Test that the project builds successfully"""
    print("Testing JAR compilation...")
    result = subprocess.run(
        "gradle clean build",
        capture_output=True,
        text=True,
        shell=True,
        cwd=Path(__file__).parent.parent.parent.parent
    )
    
    assert result.returncode == 0, f"Build failed: {result.stderr}"
    print("✓ JAR compilation successful")


def test_unit_tests_pass():
    """Test that all unit tests pass"""
    print("Testing unit tests...")
    result = subprocess.run(
        "gradle test",
        capture_output=True,
        text=True,
        shell=True,
        cwd=Path(__file__).parent.parent.parent.parent
    )
    
    assert result.returncode == 0, f"Tests failed: {result.stderr}"
    assert "BUILD SUCCESS" in result.stdout or "passed" in result.stdout
    print("✓ All unit tests passed")


if __name__ == "__main__":
    test_jar_compilation()
    test_unit_tests_pass()
    print("\n✓ All integration tests passed!")
